package components.table

import classes.TimeTable
import classes.TypeOfWeek
import classes.UpdateSchedule
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import react.*
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML
import web.html.HTMLInputElement
import web.html.InputType


external interface modeProps : Props {
    var _mode: TypeOfWeek
    var _setMode: StateSetter<TypeOfWeek>
}

val CMode = FC<modeProps>("Table") { props ->
    val weekList = listOf(TypeOfWeek.upWeek, TypeOfWeek.lowWeek)

    val refs = weekList.map {
        useRef<HTMLInputElement>()
    }
    val action: MouseEventHandler<*> = {
        refs
            .find { it.current?.checked ?: false }
            ?.current?.value
            ?.let {
                println(props._mode)
                println(it)
                if (it != props._mode.name) {
                    if (it == TypeOfWeek.upWeek.name) {
                        props._setMode(TypeOfWeek.upWeek)
                    } else {
                        props._setMode(TypeOfWeek.lowWeek)
                    }
                }
            }
    }

    weekList.mapIndexed { index, week ->
        ReactHTML.span {
            ReactHTML.input {
                type = InputType.radio
                value = week.name
                ref = refs[index]
                onClick = action
                name = "outputMode"
                checked = props._mode.name == week.name
            }
            + week.name
        }
    }

}