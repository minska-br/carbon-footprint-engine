package br.com.footprint.carbon.infrastructure.repositories

import br.com.footprint.carbon.domain.CalculationRequest
import br.com.footprint.carbon.domain.CalculationRequestRepository
import br.com.footprint.carbon.domain.CalculationRequestStatus
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.ktor.features.NotFoundException
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue

private const val COLLECTION_NAME = "CalculationRequest"

class CalculationRequestRepositoryImpl(
    database: MongoDatabase
) : CalculationRequestRepository {
    private val collection: MongoCollection<CalculationRequest> = database.getCollection(
        COLLECTION_NAME,
        CalculationRequest::class.java
    )

    override fun saveCalculationRequest(calculationRequest: CalculationRequest) {
        collection.insertOne(calculationRequest)
    }

    override fun findAll(): List<CalculationRequest> =
        collection.find().sort(BasicDBObject("startTime", -1)).toList()

    override fun findById(id: String): CalculationRequest =
        collection.findOne(CalculationRequest::requestId eq id)
            ?: throw NotFoundException("No calculation request found for id: $id")

    override fun findByCalculationId(id: String): CalculationRequest =
        collection.findOne(CalculationRequest::calculationId eq id)
            ?: throw NotFoundException("No calculation request found for id: $id")

    override fun updateStatusByCalculationId(
        calculationId: String,
        status: CalculationRequestStatus,
        endTime: String
    ) {
        collection.updateMany(
            CalculationRequest::calculationId eq calculationId,
            listOf(
                setValue(CalculationRequest::status, status),
                setValue(CalculationRequest::endTime, endTime)
            ),
        )
    }
}
