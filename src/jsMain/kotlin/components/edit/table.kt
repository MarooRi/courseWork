package components.edit


import classes.Day
import classes.UpdateSchedule
import components.edit.lessons.CEditLesson
import components.edit.lessons.CLesson
import js.core.get
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.useParams
import react.useContext
import react.useState
import styles.Styles
import typeOfWeek
import kotlin.js.Date


external interface WeekProps : Props {
    var week: List<Day>
    var update: (UpdateSchedule) -> Unit
}

val CWeek = FC<WeekProps>("Week") { props ->
    val param = useParams()["teacher"]!!
    var position by useState(listOf(-1, -1))
    val typeOfWeek = useContext(typeOfWeek)
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
                                    props.update(
                                        UpdateSchedule(
                                            typeOfWeek,
                                            param,
                                            newWeek
                                        )
                                    )
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
                props.update(UpdateSchedule(typeOfWeek, param, buttonsChange(props.week.toMutableList(), it, position[1], position[0])))
            }
        }
    }
}