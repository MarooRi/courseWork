package org.faronovama.application.rest

import Config
import classes.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.faronovama.application.database.teachersCollection
import org.litote.kmongo.*


fun Route.groupsRoutes() {
    route(Config.groups) {
        get {
            val upWeek =
                teachersCollection.aggregate<UnwindLesson>(
                    project(
                        Table::teacher from Teacher::fullName,
                        Table::week from Teacher::table / TimeTable::upWeek,
                    ),
                    unwind("\$week"),
                    unwind("\$classes"),
                    project(
                        UnwindD::teacher from UnwindDay::teacher,
                        UnwindD::dayOfWeek from UnwindDay::week / Day::dayOfWeek,
                        UnwindD::classes from UnwindDay::week / Day::classes
                    )
                ).map { it.classes.group }.flatten().toSet()
            val lowWeek =
                teachersCollection.aggregate<UnwindLesson>(
                    project(
                        Table::teacher from Teacher::fullName,
                        Table::week from Teacher::table / TimeTable::lowWeek,
                    ),
                    unwind("\$week"),
                    project(
                        UnwindD::teacher from UnwindDay::teacher,
                        UnwindD::dayOfWeek from UnwindDay::week / Day::dayOfWeek,
                        UnwindD::classes from UnwindDay::week / Day::classes
                    ),
                    unwind("\$classes"),
                ).map { it.classes.group }.flatten().toSet()

            call.respond(upWeek + lowWeek)
        }
        get("{groupName}") {
            val group =
                call.parameters["groupName"] ?: call.respondText("No group name", status = HttpStatusCode.NotFound)

            val upWeek =
                teachersCollection.aggregate<UnwindLesson>(
                    project(
                        Table::teacher from Teacher::fullName,
                        Table::week from Teacher::table / TimeTable::upWeek,
                    ),
                    unwind("\$week"),
                    project(
                        UnwindD::teacher from UnwindDay::teacher,
                        UnwindD::dayOfWeek from UnwindDay::week / Day::dayOfWeek,
                        UnwindD::classes from UnwindDay::week / Day::classes
                    ),
                    unwind("\$classes"),
                    match(UnwindLesson::classes / Lesson::group contains (group))
                ).toList()
            val lowWeek =
                teachersCollection.aggregate<UnwindLesson>(
                    project(
                        Table::teacher from Teacher::fullName,
                        Table::week from Teacher::table / TimeTable::lowWeek,
                    ),
                    unwind("\$week"),
                    project(
                        UnwindD::teacher from UnwindDay::teacher,
                        UnwindD::dayOfWeek from UnwindDay::week / Day::dayOfWeek,
                        UnwindD::classes from UnwindDay::week / Day::classes
                    ),
                    unwind("\$classes"),
                    match(UnwindLesson::classes / Lesson::group contains (group))
                ).toList()

            call.respond(GroupTimeTable(upWeek, lowWeek))
        }
    }
}

@Serializable
private class Table(
    val teacher: String,
    val week: List<Day>
)

@Serializable
private class UnwindDay(
    val teacher: String,
    val week: Day
)

@Serializable
private class UnwindD(
    val teacher: String,
    val dayOfWeek: String,
    val classes: List<Lesson>
)
