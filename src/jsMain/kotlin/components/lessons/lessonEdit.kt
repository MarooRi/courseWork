package components.lessons

import classes.Lesson
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import web.html.InputType

external interface LessonEditProps : Props {
    var lesson: Lesson
    var save: (Lesson) -> Unit
}

val CEditLesson = FC<LessonEditProps>("EditLesson") { props ->
    var lessonName by useState(props.lesson.name)
    var group by useState(props.lesson.group)
    var classRoom by useState(props.lesson.classRoom)


    ReactHTML.div {
        ReactHTML.span {
            ReactHTML.input {
                type = InputType.text
                value = lessonName
                onChange = { lessonName = it.target.value }
            }
        }
        ReactHTML.input {
            type = InputType.text
            value = group.joinToString(",")
            onChange = { group = it.target.value.split(",").toList() }
        }
    }
    ReactHTML.input {
        type = InputType.text
        value = classRoom
        onChange = { classRoom = it.target.value }
    }
    ReactHTML.button {
        + "Сохранить"
        onClick = {
            props.save(Lesson(group, classRoom, lessonName))
        }
    }
}