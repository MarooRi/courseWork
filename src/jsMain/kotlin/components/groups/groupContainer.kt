package components.groups

import Config
import classes.GroupTimeTable
import classes.UnwindLesson
import csstype.Color
import csstype.FontWeight
import csstype.TextAlign
import emotion.react.css
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.style
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.tr
import react.useState
import styles.Styles
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText

val CGroupContainer = FC<Props>("Group container") {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("groupContainer").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.groups)
        })

    var groupName by useState("")
    if (query.isSuccess) {
        val groups = Json.decodeFromString<Set<String>>(query.data ?: "")
        h2 {
            css {
                fontWeight = FontWeight.bold
                color = Color("#333")
                textAlign = TextAlign.center
            }
            +"Расписание группы"
        }

        select {
            style {
                +Styles.selectStyle
            }
            defaultValue = ""
            onChange = {
                groupName = it.target.value
            }
            option {
                +"Выберете группу"
            }
            groups.map {
                option {
                    +it
                }
            }
        }
    }


    CSchedule {
        group = groupName
    }
}

external interface ScheduleProps : Props {
    var group: String
}

val CSchedule = FC<ScheduleProps> { props ->
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("groups", props.group).unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.groups + props.group)
        })

    if (query.isSuccess) {
        val weeks =
            try {
                Json.decodeFromString<GroupTimeTable>(query.data ?: "")
            } catch (e: Exception) {
                null
            }

        h2 {
            +"Нечетная неделя"
        }
        weeks?.let {
            CTimeProps {
                lessons = weeks.upWeek
            }
        }

        h2 {
            +"Четная неделя"
        }
        weeks?.let {
            CTimeProps {
                lessons = weeks.lowWeek
            }
        }
    }
}

external interface TimeProps : Props {
    var lessons: List<UnwindLesson>
}

val CTimeProps = FC<TimeProps> { props ->
    val daysOfWeek = listOf(
        "Понедельник",
        "Вторник",
        "Среда",
        "Четверг",
        "Пятница",
        "Суббота",
    )
    val times = listOf(
        "8:00–9:30",
        "9:45–11:15",
        "11:30–13:00",
        "13:55–15:25",
        "15:40–17:10"
    )
    table {
        ReactHTML.style {
            +Styles.tableStyle
        }

        ReactHTML.tr {
            ReactHTML.td {}
            daysOfWeek.map {
                ReactHTML.td {
                    +it
                }
            }
        }
        for (time in 0..times.lastIndex) {
            ReactHTML.tr {
                val lessonsFilter = props.lessons.filter { it.classes.time == time }

                ReactHTML.td {
                    +times[time]
                }

                daysOfWeek.map { day ->
                    val les = lessonsFilter.filter { it.dayOfWeek.contains(day) }

                    when (les.size) {
                        1 -> {
                            td {
                                +(les.first().classes.name + " " + les.first().classes.classRoom)
                            }
                        }

                        0 -> {
                            td {
                                +" --- "
                            }
                        }

                        else -> {
                            ReactHTML.td {
                                les.map {
                                    tr {
                                        td {

                                            +(it.classes.name + " " + it.classes.classRoom)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}