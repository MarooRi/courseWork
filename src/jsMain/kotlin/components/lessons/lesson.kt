package components.lessons

import classes.Lesson
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface LessonProps : Props {
    var lesson: Lesson
}

val CLesson = FC<LessonProps>("Lesson") { props ->
    ReactHTML.div {
        ReactHTML.span {
            +"${props.lesson.name} "
        }
        +props.lesson.group.joinToString(separator = ", ") { it }
    }
    ReactHTML.div {
        +props.lesson.classRoom
    }
}