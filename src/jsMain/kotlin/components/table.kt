package components

import classes.*
import components.lessons.CEditLesson
import components.lessons.CLesson
import js.core.jso
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import styles.Styles

import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import kotlin.js.Date.Companion.now
import kotlin.js.json

external interface Table : Props {
    var table: TimeTable
}

val CTable = FC<Table>("Table") { props ->
    val queryClient = useQueryClient()
    val lessons = props.table
    var position by useState(listOf(-1, -1))

    val daysOfWeek = listOf(
        "Понедельник",
        "Вторник",
        "Среда",
        "Четверг",
        "Пятница",
        "Суббота",
    )

    val times = listOf(
        "8:00 – 9:30",
        "9:45 – 11:15",
        "11:30 – 13:00",
        "13:55 – 15:25",
        "15:40 – 17:10"
    )

    val updateMutation = useMutation<HTTPResult, Any, UpdateSchedule, Any>(
        mutationFn = { item: UpdateSchedule ->
            fetch(
                Config.schedule + item.path,
                jso {
                    method = "PUT"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    body = item.json
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(arrayOf("teacherLessons").unsafeCast<QueryKey>())
            }
        }
    )

    ReactHTML.style {
        +Styles.tableStyle
    }

    ReactHTML.table {
        ReactHTML.thead {
            ReactHTML.tr {
                ReactHTML.td {}
                daysOfWeek.map {
                    ReactHTML.td {
                        +it
                    }
                }
            }
        }

        for (time in 0..times.lastIndex) {
            ReactHTML.tr {
                ReactHTML.td {
                    +times[time]
                }
                daysOfWeek.indices.map { day ->
                    ReactHTML.td {
                        if (position[0] == time && position[1] == day) {
                            CEditLesson {
                                lesson = lessons.upWeek[day].classes[time]
                                save = {
                                    val newWeek = lessons.upWeek.toMutableList()
                                    newWeek[day].classes = newWeek[day].classes.mapIndexed { index, elem ->
                                        if (time == index) it else elem
                                    }

                                    updateMutation.mutateAsync(
                                        UpdateSchedule(
                                            TypeOfWeek.upWeek,
                                            "доц. Альтман Е.А.",
                                            newWeek
                                        ), null
                                    )
                                    position = listOf(-1, -1)
                                }
                                key = now().toString()
                            }

                        } else {
                            ReactHTML.div {
                                CLesson {
                                    lesson = lessons.upWeek[day].classes[time]
                                }
                                onClick = {
                                    position = listOf(time, day)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (position[0] != -1 || position[1] != -1) {
        CButtons {
            save = {
                val newWeek = lessons.upWeek.toMutableList()

                if (it == TypeOfButton.Up) {
                    newWeek[position[1]] = lessons.upWeek[position[1]].copy(
                        classes = swapLessons(
                            lessons.upWeek[position[1]].classes.toMutableList(),
                            position[0],
                            position[0] - 1
                        )
                    )

                    updateMutation.mutateAsync(
                        UpdateSchedule(TypeOfWeek.upWeek, "доц. Альтман Е.А.", newWeek.toList()),
                        null
                    )
                }
            }
        }
    }
}


fun swapLessons(lessons: MutableList<Lesson>, from: Int, to: Int): List<Lesson> {
    lessons.ifEmpty { return emptyList() }
    lessons[from] = lessons[to].also { lessons[to] = lessons[from] }
    return lessons.toList()
}