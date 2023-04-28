package components

import Config
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.FormMethod
import react.dom.html.ReactHTML
import web.html.InputType

val reader = FC<Props> {

    ReactHTML.form {
        name = "file"
        encType = "multipart/form-data"
        method = FormMethod.post
        action = "${Config.schedule}loadExcel"
        ReactHTML.div {
            ReactHTML.label {
                form = "file"
                +"Выберете файл"
            }
            ReactHTML.input {
                type = InputType.file
                name = "file"
                accept = ".xlsx"
            }
        }
        ReactHTML.button {
            type = ButtonType.submit
            +"Submit"
        }
    }
}