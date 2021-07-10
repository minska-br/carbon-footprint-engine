package br.com.footprint.carbon.infrastructure.repositories

import br.com.footprint.carbon.domain.Calculation
import br.com.footprint.carbon.domain.CalculationRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

private const val COLLECTION_NAME = "Calculation"

class CalculationRepositoryImpl(
    database: MongoDatabase
) : CalculationRepository {
    private val collection: MongoCollection<Calculation> = database.getCollection(
        COLLECTION_NAME,
        Calculation::class.java
    )

    override fun save(calculation: Calculation) {
        collection.insertOne(calculation)
    }

    override fun findById(id: String): Calculation? =
        collection.findOne(Calculation::id eq id)
}
