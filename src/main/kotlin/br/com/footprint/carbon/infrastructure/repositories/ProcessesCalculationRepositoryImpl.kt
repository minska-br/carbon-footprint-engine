package br.com.footprint.carbon.infrastructure.repositories

import br.com.footprint.carbon.domain.ProcessesCalculation
import br.com.footprint.carbon.domain.ProcessesCalculationRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.ktor.features.NotFoundException
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

private const val COLLECTION_NAME = "ProcessesCalculation"

class ProcessesCalculationRepositoryImpl(
    database: MongoDatabase
) : ProcessesCalculationRepository {
    private val collection: MongoCollection<ProcessesCalculation> = database.getCollection(
        COLLECTION_NAME,
        ProcessesCalculation::class.java
    )

    override fun save(processesCalculation: ProcessesCalculation) {
        collection.insertOne(processesCalculation)
    }

    override fun findByCalculationId(id: String): ProcessesCalculation =
        collection.findOne(ProcessesCalculation::calculationId eq id)
            ?: throw NotFoundException("No processes calculation found for calculation id: $id")
}
