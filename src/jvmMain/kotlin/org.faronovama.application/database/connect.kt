package org.example.application.database

import com.mongodb.ExplainVerbosity
import com.mongodb.client.FindIterable
import org.json.JSONObject

import org.litote.kmongo.KMongo
import org.litote.kmongo.json

val client = KMongo.createClient(connectionString = "mongodb://root:example@127.0.0.1:27017")
//al client = KMongo.createClient(connectionString = "mongodb://localhost:8081")

val mongoDatabase = client.getDatabase("schedule")

fun prettyPrintJson(json: String) =
    println(
        JSONObject(json)
            .toString(4)
    )

fun prettyPrintCursor(cursor: Iterable<*>) =
    prettyPrintJson("{ result: ${cursor.json} }")

fun prettyPrintExplain(cursor: FindIterable<*>) =
    prettyPrintJson(cursor.explain(ExplainVerbosity.EXECUTION_STATS).json)