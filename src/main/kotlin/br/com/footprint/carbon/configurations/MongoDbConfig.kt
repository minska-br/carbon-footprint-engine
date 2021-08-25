package br.com.footprint.carbon.configurations

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo

fun mongoConnection(
    connectionString: ConnectionString,
) = KMongo.createClient(
    MongoClientSettings
        .builder()
        .applyConnectionString(connectionString)
        .build()
)

fun mongoDatabase(
    connectionString: ConnectionString,
    databaseName: String
): MongoDatabase = mongoConnection(
    connectionString = connectionString
).getDatabase(databaseName)
