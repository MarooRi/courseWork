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
        week = if (type == TypeOfWeek.upWeek) lessons.upWeek else lessons.lowWeek
        update = {
            updateMutation.mutateAsync(it, null)
        }
    }
}


fun buttonsChange(lessons: MutableList<Day>, type: TypeOfButton, x: Int, y: Int): List<Day> {
    when (type) {
        TypeOfButton.Up -> {
            val newClasses = lessons[x].classes.toMutableList()
            newClasses[y] = newClasses[y - 1].also {
                newClasses[y - 1] = newClasses[y]
            }

            lessons[x] = lessons[x].copy(
                classes = newClasses
            )
        }

        TypeOfButton.Down -> {
            val newClasses = lessons[x].classes.toMutableList()
            newClasses[y] = newClasses[y + 1].also {
                newClasses[y + 1] = newClasses[y]
            }

            lessons[x] = lessons[x].copy(
                classes = newClasses
            )
        }

        TypeOfButton.Left -> {
            val firstLessonsList = lessons[x].classes.toMutableList()
            val secondLessonsList = lessons[x - 1].classes.toMutableList()

            val firstLesson = firstLessonsList[y]
            val secondLesson = secondLessonsList[y]

            lessons[x] = lessons[x].copy(
                classes = firstLessonsList.apply { firstLessonsList[y] = secondLesson }
            )

            lessons[x - 1] = lessons[x - 1].copy(
                classes = secondLessonsList.apply { secondLessonsList[y] = firstLesson }
            )
        }

        TypeOfButton.Right -> {
            val firstLessonsList = lessons[x].classes.toMutableList()
            val secondLessonsList = lessons[x + 1].classes.toMutableList()

            val firstLesson = firstLessonsList[y]
            val secondLesson = secondLessonsList[y]

            lessons[x] = lessons[x].copy(
                classes = firstLessonsList.apply { firstLessonsList[y] = secondLesson }
            )

            lessons[x + 1] = lessons[x + 1].copy(
                classes = secondLessonsList.apply { secondLessonsList[y] = firstLesson }
            )
        }
        else -> {}
    }

    return lessons.toList()
}