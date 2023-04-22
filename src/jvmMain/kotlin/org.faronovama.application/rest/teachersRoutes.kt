package org.faronovama.application.rest

import Config
import classes.Teacher
import classes.TimeTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToJsonElement
import org.example.application.database.prettyPrintCursor
import org.faronovama.application.database.teacherRepo
import org.faronovama.application.database.teachersCollection
import org.litote.kmongo.*


fun Route.teachersRoutes() {
    route(Config.teachers) {
        get {
            val teachers = teachersCollection.find().toList()
            
            if (teachers.isEmpty()) {
                call.respondText("No element found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(teachers)
            }
        }
        get("{teacherName}") {
            val teacherName = call.parameters["teacherName"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val teacher = teachersCollection.findOne(Teacher::fullName eq teacherName) ?: return@get call.respondText(
                "Нет учителя с таким именем",
                status = HttpStatusCode.NotFound
            )

            call.respond(teacher)
        }
        get("allNames"){
            val teachersNames = teachersCollection.projection(Teacher::fullName).toList()

            if(teachersNames.isEmpty()){
                call.respondText("No element found", status = HttpStatusCode.NotFound)
            }else {
                call.respond(teachersNames)
            }
        }
    }
}
