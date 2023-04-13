package org.faronovama.application

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.html.*
import org.faronovama.application.database.teacherRepo
import org.faronovama.application.database.teachersCollection
import org.faronovama.application.rest.teachersRoutes

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
    //teachersCollection.insertOne(teacherRepo)
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
            +"Hello from Ktor"
        }
        script(src = "/static/courseWork.js") {}
    }
}

fun Application.rest() {
    routing {
        teachersRoutes()
    }
}

