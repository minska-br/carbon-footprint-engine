package br.com.footprint.carbon.infrastructure.repositories

import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.domain.CalculationRequest
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import java.util.UUID

private const val COLLECTION_NAME = "CalculationRequest"

data class CalculationRequestNotFoundException(val msg: String) : Exception(msg)

class CalculationRepositoryImpl(
    database: MongoDatabase
) : CalculationRepository  {
    private val collection: MongoCollection<CalculationRequest> = database.getCollection(
        COLLECTION_NAME,
        CalculationRequest::class.java
    )

    override fun saveCalculationRequest(calculationRequest: CalculationRequest) {
        collection.insertOne(calculationRequest)
    }

    override fun findAll(): List<CalculationRequest> =
        collection.find().toList()


    override fun findCalculationRequestById(id: UUID): CalculationRequest =
        collection.find(CalculationRequest::id eq id.toString()).first()
            ?: throw CalculationRequestNotFoundException("No calculation request found for id $id")
}
