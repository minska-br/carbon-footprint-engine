package br.com.footprint.carbon.infrastructure.repositories

import br.com.footprint.carbon.domain.CalculationCompletedEvent
import br.com.footprint.carbon.domain.CalculationRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.ktor.features.NotFoundException
import org.litote.kmongo.eq

private const val COLLECTION_NAME = "ProcessCalculations"

class CalculationRepositoryImpl (
    database: MongoDatabase
) : CalculationRepository {
    private val collection: MongoCollection<CalculationCompletedEvent> = database.getCollection(
        COLLECTION_NAME,
        CalculationCompletedEvent::class.java
    )

    override fun saveCalculationCompletedEvent(event: CalculationCompletedEvent) {
        collection.insertOne(event)
    }

    override fun findByCalculationId(id: String): CalculationCompletedEvent =
        collection.find(CalculationCompletedEvent::calculationId eq id).first()
            ?: throw NotFoundException("No process calculation found for calculation id: $id")
}
