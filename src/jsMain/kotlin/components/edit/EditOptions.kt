package components.edit

import Config
import components.CFooter
import csstype.*
import emotion.react.css
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useRef
import react.useState
import styles.CArrowL
import styles.Styles
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement


val CEditOpt = FC<Props> {
   val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachersNames").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText("${Config.teachers}allNames")
        })

    val selectRef = useRef<HTMLSelectElement>()
    var input by useState("")

    if (query.isSuccess) {
        val data = Json.decodeFromString<List<String>>(query.data ?: "")
        ReactHTML.div {
            css{
                textAlign = TextAlign.center
            }
            ReactHTML.label{
                css {
                    textAlign = TextAlign.center
                    fontSize = 40.px
                    fontFamily = FontFamily.cursive
                }
                +"Редактирование"
            }
        }
        ReactHTML.div {
            ReactHTML.label {
                css {
                    fontSize = 20.px
                    border = None.none
                    borderRadius = 30.px
                }
                +"Выберите преподавателя"
            }
        }

        select{
            ref = selectRef
            option {
               // disabled = true
                +" "
            }
            data.map{
                option{
                    +it
                    value = it
                }
            }
            onChange = {
                selectRef.current?.value?.let {
                    input = it
                }
            }
        }
        form{
          action = "/#/editOpt/${input}"
           // console.log(input)
            ReactHTML.button {
                ReactHTML.style {
                    +Styles.butStyle
                }
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
    CArrowL{
        path = "/"
    }
    CFooter{}
}


