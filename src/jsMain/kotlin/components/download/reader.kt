package components.download

import Config
import components.CFooter
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.FormMethod
import react.dom.html.ReactHTML
import styles.CArrowL
import web.html.InputType

val reader = FC<Props> {
    ReactHTML.div {
        ReactHTML.h1 {
            css {
                fontSize = 70.px
                fontFamily = FontFamily.cursive
                textEmphasisColor = Color("fadadd")
                textAlign = TextAlign.center
            }
            +"Кафедральное расписание"
        }
    }
    ReactHTML.form {
        name = "file"
        encType = "multipart/form-data"
        method = FormMethod.post
        action = "${Config.schedule}loadExcel"
        ReactHTML.div {
            css {
                textAlign = TextAlign.center
            }
            ReactHTML.label {
                ReactHTML.input {
                    css {
                        textAlign = TextAlign.center
                        display = None.none
                    }
                    name = "file"
                    type = InputType.file
                    accept = ".xlsx"
                }
                css {
                    cursor = Cursor.pointer
                    textAlign = TextAlign.center
                    borderRadius = 70.px
                    fontSize = 20.px
                    padding = 10.px
                    background = NamedColor.pink
                    hover {
                        background = rgb(230, 156, 156)
                    }
                }
                +"Загрузить"
            }
            ReactHTML.button {
                css {
                    background = NamedColor.white
                    border = None.none
                    fontSize = 30.px
                    hover {
                        background = NamedColor.whitesmoke
                    }
                }
                type = ButtonType.submit
                +"☑"
            }
        }
    }
    ReactHTML.div {
        css {
            padding = 10.px
            textAlign = TextAlign.center
            fontSize = 20.px
        }
        ReactHTML.label {
            +"Загрузить расписание в формате .xlsx"
        }
    }
    CArrowL{
        path = "/"
    }
    CFooter{}
}