package styles

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div


external interface arowRProps : Props {
    var path: String
}

val CArrowR = FC<arowRProps> { props ->
    div {
        css {
            position = Position.absolute
            bottom = 50.px
            right = 50.px
        }
        ReactHTML.form {
            action = props.path
            ReactHTML.button {
                css {
                    borderRadius = 70.px
                    fontFamily = FontFamily.monospace
                    background = NamedColor.pink
                    fontSize = 30.px
                    padding = 20.px
                    border = None.none
                    hover {
                        background = rgb(230, 156, 156)
                    }
                }
                +"→"
            }
        }

    }
}