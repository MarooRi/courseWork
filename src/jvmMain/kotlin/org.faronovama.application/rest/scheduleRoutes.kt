package org.faronovama.application.rest

import Config
import classes.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.faronovama.application.database.loadSelectTeachers
import org.faronovama.application.database.readListTeachers
import org.faronovama.application.database.teachersCollection
import org.litote.kmongo.*
import java.io.File


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

            val teachers = readListTeachers("uploads/$fileName")

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
            val command = call.receive<AddTeachersToDatabase>()
            val fileName =
                call.parameters["fileName"] ?: call.respondText("No file name", status = HttpStatusCode.NotFound)

            if (command.teachers.isEmpty()) {
                call.respondText("No teachers name", status = HttpStatusCode.NotFound)
            } else {
                val teachers = loadSelectTeachers(command.teachers, "./uploads/${fileName}")

                when (command.typeOfAction) {
                    TypeOfAction.AddTeacher -> {
                        teachersCollection.insertMany(teachers)
                    }

                    TypeOfAction.SumSchedule -> {
                        teachers.map {
                            val teacher = teachersCollection.findOne(Teacher::fullName eq it.fullName)
                                ?: return@post call.respondText("No teacher in BD", status = HttpStatusCode.NotFound)

                            val newScheduleUpWeek = teacher.table.upWeek.map { day ->
                                Day(
                                    day.dayOfWeek,
                                    day.classes + it.table.upWeek.find { it.dayOfWeek == day.dayOfWeek }!!.classes
                                )
                            }

                            val newScheduleLowWeek = teacher.table.lowWeek.map { day ->
                                Day(
                                    day.dayOfWeek,
                                    day.classes + it.table.upWeek.find { it.dayOfWeek == day.dayOfWeek }!!.classes
                                )
                            }

                            teachersCollection.updateOne(
                                Teacher::fullName eq it.fullName,
                                setValue(Teacher::table, TimeTable(newScheduleUpWeek, newScheduleLowWeek))
                            )
                        }
                    }

                    TypeOfAction.ReplaceSchedule -> {
                        teachers.map {
                            teachersCollection.updateOne(
                                Teacher::fullName eq it.fullName,
                                setValue(
                                    Teacher::table,
                                    it.table
                                )
                            )
                        }
                    }
                }

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

fun checkUpdateSchedule(week: UpdateSchedule): Boolean {
    val regex = Regex("\\d-\\d{3}[а-я]?")

    if (week.teacherName.isBlank()) return false

    week.week.map {
        if (it.classes.isEmpty()) return false

        it.classes.map { les ->
            if (les.classRoom.isBlank() || les.group.isEmpty() || les.name.isBlank()) return false

            if (regex.findAll(les.classRoom).toList().isEmpty()) return false
        }
    }
    return true
}