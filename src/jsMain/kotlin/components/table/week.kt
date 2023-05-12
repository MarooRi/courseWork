package components.table

import classes.Day
import classes.TypeOfButton
import classes.TypeOfWeek
import classes.UpdateSchedule
import components.CButtons
import components.lessons.CEditLesson
import components.lessons.CLesson
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import styles.Styles
import kotlin.js.Date

external interface WeekProps : Props {
    var week: List<Day>
    var update: (UpdateSchedule) -> Unit
}

val CWeek = FC<WeekProps>("Week") { props ->
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
                                lesson = props.week[day].classes[time]
                                save = {
                                    val newWeek = props.week.toMutableList()
                                    newWeek[day].classes = newWeek[day].classes.mapIndexed { index, elem ->
                                        if (time == index) it else elem
                                    }
                                    props.update(UpdateSchedule(
                                        TypeOfWeek.upWeek,
                                        "доц. Альтман Е.А.",
                                        newWeek
                                    ))
                                    position = listOf(-1, -1)
                                }
                                key = Date.now().toString()
                            }

                        } else {
                            ReactHTML.div {
                                CLesson {
                                    lesson = props.week[day].classes[time]
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
                val newWeek = props.week.toMutableList()

                if (it == TypeOfButton.Up) {
                    newWeek[position[1]] = props.week[position[1]].copy(
                        classes = swapLessons(
                            props.week[position[1]].classes.toMutableList(),
                            position[0],
                            position[0] - 1
                        )
                    )
                    props.update(UpdateSchedule(TypeOfWeek.upWeek, "доц. Альтман Е.А.", newWeek.toList()))
                }
            }
        }
    }
}