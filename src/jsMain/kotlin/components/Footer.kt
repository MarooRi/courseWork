package components

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.label
import styles.Styles


val CFooter = FC<Props> {

    div {
        ReactHTML.style {
            +Styles.testStyle
        }
        ReactHTML.footer {
            div{
                ReactHTML.ul {
                    ReactHTML.li {
                        ReactHTML.a {
                            href = "#"
                            +"home"
                        }
                    }
                    ReactHTML.li {
                        ReactHTML.a {
                            href = "https://github.com/MarooRi/courseWork"
                            +"GitHub"
                        }
                    }
                }
            }

            div{
                label {
                    +"©2023 Sait raspisaniya for cafedra AISU | FarMaria MarKsenia BotZahar "
                }
            }

        }

    }
}