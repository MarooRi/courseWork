package components.table

import Config
import classes.*
import components.CButtons
import components.lessons.CEditLesson
import components.lessons.CLesson
import js.core.jso
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useContext
import react.useState
import styles.Styles
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import typeOfWeek
import kotlin.js.Date.Companion.now
import kotlin.js.json

external interface Table : Props {
    var table: TimeTable
}

val CTable = FC<Table>("Table") { props ->
    val queryClient = useQueryClient()
    val lessons = props.table

    val type = useContext(typeOfWeek)

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

    CWeek {
        week = if(type == TypeOfWeek.upWeek) lessons.upWeek else lessons.lowWeek
        update = {
            updateMutation.mutateAsync(it, null)
        }
    }
}


fun swapLessons(lessons: MutableList<Lesson>, from: Int, to: Int): List<Lesson> {
    lessons.ifEmpty { return emptyList() }
    lessons[from] = lessons[to].also { lessons[to] = lessons[from] }
    return lessons.toList()
}