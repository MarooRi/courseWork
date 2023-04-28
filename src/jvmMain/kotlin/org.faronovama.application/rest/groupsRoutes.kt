package org.faronovama.application.rest

import classes.Day
import classes.Lesson
import classes.Teacher
import classes.TimeTable
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
                        Table::week from Teacher::table / TimeTable::upWeek,
                    ),
                    unwind("\$week"),
                    project(UnwindD::classes from UnwindDay::week / Day::classes),
                    unwind("\$classes")
                ).toList().map { it.classes.group }.toSet()
            //println(upWeek)
            //println(System.currentTimeMillis())
            /*prettyPrintCursor(teachersCollection.aggregate<UnwindTimeTable>(
                project(TimeTable::upWeek from Teacher::table / TimeTable::upWeek,
                    TimeTable::lowWeek from Teacher::table / TimeTable::lowWeek,
                    ),
                unwind("\$upWeek"),
                unwind("\$lowWeek")
            )
            )*/
            /*println(System.currentTimeMillis())
            val l = teachersCollection.aggregate<TimeTable>(
                project(TimeTable::upWeek from Teacher::table / TimeTable::upWeek,
                    TimeTable::lowWeek from Teacher::table / TimeTable::lowWeek,
                )
            ).toList().map {
                (it.lowWeek + it.lowWeek).map { it.classes.map { it.group } }
            }.flatten().flatten().toSet()
            println(l)*/

            /*val groups = teachersCollection.aggregate<String>(
                project(Teacher::table),
                project(TimeTable::lowWeek, TimeTable::upWeek),
                unwind("\$upWeek"),
                unwind("\$lowWeek"),
                unwind("\$classes"),
                project(Lesson::group)
            ).toList()*/
        }
    }
}

@Serializable
private class Table(
    val week: List<Day>,
)
@Serializable
private class UnwindDay(
    val week: Day
)
@Serializable
private class UnwindD(
    val classes: List<Lesson>
)
@Serializable
private class UnwindLesson(
    val classes: Lesson
)
