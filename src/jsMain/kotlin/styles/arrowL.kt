package styles

import react.FC
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.p
import web.dom.document


val CArrowL = FC<arowRProps> { props ->
    ReactHTML.div {
        ReactHTML.style {
            +Styles.butStyle
        }
        p{
            ReactHTML.form {
                document.body.classList.add("but1")
                action = props.path
                ReactHTML.button {
                    +"←"
                }
            }
        }

    }
}