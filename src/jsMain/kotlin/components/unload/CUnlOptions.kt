package components.unload

import Config
import components.CFooter
import csstype.TextAlign
import emotion.react.css
import js.core.jso
import react.FC
import react.Props
import react.dom.html.ReactHTML
import styles.CArrowL
import styles.Styles
import tanstack.react.query.useMutation
import tools.HTTPResult
import tools.fetch
import kotlin.js.json


val CUnity = FC<Props> {
    val updateMutation = useMutation<HTTPResult, Any, String, Any>(
        mutationFn = { path ->
            fetch(
                Config.schedule + "getExcelFile",
                jso {
                    method = "POST"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    body = path
                }
            )
        }
    )

    ReactHTML.div {
        css{
            textAlign = TextAlign.center
        }
        ReactHTML.h1{
            +"Выгрузить расписание"
        }
        ReactHTML.div{
            ReactHTML.form{
                //action = "/#/download"
                ReactHTML.button{
                    ReactHTML.style {
                        +Styles.buttonStyle
                    }
                    +"Кафедра"
                    onClick = {
                        updateMutation.mutateAsync("C:\\Users\\Public\\Downloads", null)
                    }
                }
            }
        }

        ReactHTML.div{
            ReactHTML.form{
                action = "/#/group"
                ReactHTML.button{
                    ReactHTML.style {
                        +Styles.buttonStyle
                    }
                    +"Группы"
                }

            }
        }
        CArrowL{
            path = "/"
        }

        CFooter{}
    }
}

