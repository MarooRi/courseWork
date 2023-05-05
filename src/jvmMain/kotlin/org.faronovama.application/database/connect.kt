package org.faronovama.application.database

import classes.Teacher
import com.mongodb.ExplainVerbosity
import com.mongodb.client.FindIterable
import org.json.JSONObject

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.json

val client = KMongo.createClient(connectionString = "mongodb://mongoadmin:mongoadmin@127.0.0.1:27017")

val mongoDatabase = client.getDatabase("schedule")
val teachersCollection = mongoDatabase.getCollection<Teacher>("teachers")

fun prettyPrintJson(json: String) =
    println(
        JSONObject(json)
            .toString(4)
    )

fun prettyPrintCursor(cursor: Iterable<*>) =
    prettyPrintJson("{ result: ${cursor.json} }")

fun prettyPrintExplain(cursor: FindIterable<*>) =
    prettyPrintJson(cursor.explain(ExplainVerbosity.EXECUTION_STATS).json)