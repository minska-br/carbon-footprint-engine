package br.com.footprint.carbon.infrastructure.repositories

import br.com.footprint.carbon.domain.Calculation
import br.com.footprint.carbon.domain.CalculationRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.setValue

private const val COLLECTION_NAME = "Calculation"

class CalculationRepositoryImpl(
    database: MongoDatabase
) : CalculationRepository {
    private val collection: MongoCollection<Calculation> = database.getCollection(
        COLLECTION_NAME,
        Calculation::class.java
    )

    override fun saveOrUpdate(calculation: Calculation) {
        collection.findOne(Calculation::id eq calculation.id)?.also {
            collection.updateMany(
                Calculation::id eq calculation.id,
                listOf(
                    setValue(Calculation::processes, calculation.processes),
                    setValue(Calculation::calculatedPercentage, calculation.calculatedPercentage),
                    setValue(Calculation::totalCarbonFootprint, calculation.totalCarbonFootprint),
                    setValue(Calculation::conversions, calculation.conversions)
                )
            )
        } ?: collection.insertOne(calculation)
    }

    override fun findById(id: String): Calculation? =
        collection.findOne(Calculation::id eq id)
}
