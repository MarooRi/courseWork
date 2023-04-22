package org.faronovama.application.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.scheduleRoutes() {
    route(Config.schedule){
        post("loadSchedule"){
            val nameFile = call.receiveParameters()["file"] ?: return@post call.respondText(
                    "No file name",
                status = HttpStatusCode.NotFound)
            if (!nameFile.endsWith(".xlsx")) {
                return@post call.respondRedirect("/")
            }

            println(nameFile)
        }
    }
}