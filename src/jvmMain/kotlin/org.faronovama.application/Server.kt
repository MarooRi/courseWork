package org.faronovama.application

import classes.Teacher
import com.mongodb.client.model.IndexOptions
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.html.*
import org.faronovama.application.database.teachersCollection
import org.faronovama.application.rest.groupsRoutes
import org.faronovama.application.rest.scheduleRoutes
import org.faronovama.application.rest.teachersRoutes
import org.litote.kmongo.ensureIndex
import java.time.Duration

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "127.0.0.1",
        watchPaths = listOf("classes")
    ) {
        main()
    }.start(wait = true)
}

fun Application.main() {
    static()
    rest()
    teachersCollection.ensureIndex(Teacher::fullName, indexOptions = IndexOptions().unique(true) )
}


fun Application.static() {
    install(createApplicationPlugin("DelayEmulator") {
        onCall {
            delay(1000L)
        }
    })
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
    install(ContentNegotiation) {
        json()
    }
}

fun HTML.index() {
    head {
        title("Приложение для расписания")
    }
    body {
        div {
            id = "root"
            +"Идет загрузка приложения"
        }
        script(src = "/static/courseWork.js") {}
    }
}

fun Application.rest() {
    routing {
        teachersRoutes()
        scheduleRoutes()
        groupsRoutes()
    }
}

