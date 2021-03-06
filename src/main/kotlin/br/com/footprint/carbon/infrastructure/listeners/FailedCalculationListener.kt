package br.com.footprint.carbon.infrastructure.listeners
import br.com.footprint.carbon.domain.CalculationRequestRepository
import br.com.footprint.carbon.domain.CalculationRequestStatus
import br.com.footprint.carbon.domain.Miscalculation
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.future.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.slf4j.LoggerFactory
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.net.URI
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class FailedCalculationListener(
    sqsUri: String,
    private val sqsUrl: String,
    private val calculationRequestRepository: CalculationRequestRepository
) : CoroutineScope {

    companion object {
        private const val N_WORKERS = 2
        private const val VISIBILITY_TIMEOUT = 10
        private const val WAIT_TIME_SECONDS = 20
        private const val MAX_NUMBER_OF_MESSAGES = 10
    }

    private var logger = LoggerFactory.getLogger(FailedCalculationListener::class.java)

    private val sqs = SqsAsyncClient.builder()
        .region(Region.US_EAST_2)
        .endpointOverride(URI(sqsUri))
        .build()

    private val supervisorJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + supervisorJob

    fun start() = launch {
        val messageChannel = Channel<Message>()
        repeat(N_WORKERS) { launchWorker(messageChannel) }
        launchMsgReceiver(messageChannel)
    }

    private fun CoroutineScope.launchMsgReceiver(channel: SendChannel<Message>) = launch {
        repeatUntilCancelled {
            val receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsUrl)
                .waitTimeSeconds(WAIT_TIME_SECONDS)
                .maxNumberOfMessages(MAX_NUMBER_OF_MESSAGES)
                .build()

            val messages = sqs.receiveMessage(receiveRequest).await().messages()

            messages.forEach {
                channel.send(it)
            }
        }
    }

    private fun CoroutineScope.launchWorker(channel: ReceiveChannel<Message>) = launch {
        repeatUntilCancelled {
            for (msg in channel) {
                try {
                    processMsg(msg)
                    deleteMessage(msg)
                } catch (ex: Exception) {
                    logger.error("${Thread.currentThread().name} exception trying to process message ${msg.body()}", ex)
                    changeVisibility(msg)
                }
            }
        }
    }

    private fun processMsg(message: Message) =
        jacksonObjectMapper().readValue(message.body(), Miscalculation::class.java).also {
            logger.info("Update Calculation Request status")
            calculationRequestRepository.updateStatusByCalculationId(
                calculationId = it.calculationId,
                status = CalculationRequestStatus.ERROR,
                endTime = LocalDateTime.now().toString()
            )
            logger.info("Saved event !")
        }

    private suspend fun deleteMessage(message: Message) {
        sqs.deleteMessage { req ->
            req.queueUrl(sqsUrl)
            req.receiptHandle(message.receiptHandle())
        }.await()
    }

    private suspend fun changeVisibility(message: Message) {
        sqs.changeMessageVisibility { req ->
            req.queueUrl(sqsUrl)
            req.receiptHandle(message.receiptHandle())
            req.visibilityTimeout(VISIBILITY_TIMEOUT)
        }.await()
    }

    private suspend fun CoroutineScope.repeatUntilCancelled(block: suspend () -> Unit) {
        while (isActive) {
            try {
                block()
                yield()
            } catch (ex: CancellationException) {
                logger.error("coroutine on ${Thread.currentThread().name} cancelled", ex)
            } catch (ex: Exception) {
                logger.error("${Thread.currentThread().name} failed with {$ex}. Retrying...", ex)
            }
        }
    }
}
