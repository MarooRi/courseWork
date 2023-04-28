package org.faronovama.application.rest

import Config
import classes.Teacher
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
