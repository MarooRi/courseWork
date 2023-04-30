package org.faronovama.application.rest

import classes.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.faronovama.application.database.teachersCollection
import java.io.File
import org.faronovama.application.database.readTeachersFromExcel
import org.litote.kmongo.*


fun Route.scheduleRoutes() {
    route(Config.schedule) {
        get("{teacherName}") {
            val teacherName = call.parameters["teacherName"] ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )

            val teacher = teachersCollection.findOne(Teacher::fullName eq teacherName)
                ?: return@get call.respondText("Нет учителя с таким именем", status = HttpStatusCode.NotFound)

            call.respond(teacher.table)
        }
        get("namesFromExcel/{fileName}") {
            val fileName = call.parameters["fileName"] ?: return@get call.respondText(
                "No file name",
                status = HttpStatusCode.NotFound
            )

            val teachers = readTeachersFromExcel("uploads/$fileName").map { it.fullName }
            if (teachers.isEmpty()) {
                return@get call.respondText("Empty file", status = HttpStatusCode.NotFound)
            } else {
                call.respond(teachers)
            }
        }
        post("loadExcel") {
            val multiPartData = call.receiveMultipart()
            var fileName = ""

            multiPartData.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        File("uploads/$fileName").writeBytes(fileBytes)
                    }

                    else -> {}
                }
                part.dispose()
            }
            if (!fileName.endsWith(".xlsx")) {
                File("uploads/$fileName").delete()
                return@post call.respondRedirect("/")
            } else {
                return@post call.respondRedirect("/#/home/${fileName}")
            }
        }
        post("loadSchedule/{fileName}") {
            val teachersName = call.receive<List<String>>()
            val fileName =
                call.parameters["fileName"] ?: call.respondText("No file name", status = HttpStatusCode.NotFound)

            if (teachersName.isEmpty()) {
                call.respondText("No teachers name", status = HttpStatusCode.NotFound)
            } else {
                val teachers = readTeachersFromExcel("./uploads/${fileName}")
                teachersCollection.insertMany(teachers)
            }
        }
        put("updateLesson") {
            val updateData = call.receive<UpdateSchedule>()

            val classesInJson = Json.encodeToJsonElement(updateData.week)
            teachersCollection.updateMany(
                "{ 'fullName': '${updateData.teacherName}'}",
                " {'\$set': {'table.${updateData.typeOfWeek}':" +
                        " $classesInJson}}"
            )
        }
    }
}