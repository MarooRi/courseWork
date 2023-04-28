package components

import classes.TypeOfButton
import csstype.Display
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div

external interface ButtonsProps : Props {
    var save: (TypeOfButton) -> Unit
}

val CButtons = FC<ButtonsProps> { props ->
    ReactHTML.div {
        css {
            display = Display.block
        }
        div {
            ReactHTML.button {
                +"⬜️"
                disabled = true
            }
            ReactHTML.button {
                +" \uD83D\uDD3C "
                onClick = {
                    props.save(TypeOfButton.Up)
                }
            }
            ReactHTML.button {
                +"⬜️"
                disabled = true
            }
        }

        ReactHTML.div {

            ReactHTML.button {
                +"◀️ "
                onClick = {
                    props.save(TypeOfButton.Left)
                }
            }


            ReactHTML.button {
                +"⬜️"
                disabled = true
            }
            ReactHTML.button {
                +"▶️ "
                onClick = {
                    props.save(TypeOfButton.Right)
                }
            }
        }
        div {
            ReactHTML.button {
                +"⬜️"
                disabled = true
            }
            ReactHTML.button {
                +" \uD83D\uDD3D "
                onClick = {
                    props.save(TypeOfButton.Down)
                }
            }
            ReactHTML.button {
                +"⬜️"
                disabled = true
            }
        }

    }
}