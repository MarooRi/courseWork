package components.download

import Config
import classes.AddTeachersToDatabase
import classes.TypeOfAction
import components.CFooter
import csstype.*
import emotion.react.css
import js.core.get
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.style
import react.router.useParams
import react.useState
import styles.CArrowL
import styles.Styles
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import web.dom.document
import web.html.HTMLInputElement
import web.html.InputType
import kotlin.js.json


fun selectAll(teachers: List<String>, checked: Boolean, checkList: MutableList<Boolean>) {
    val checkboxes = document.getElementsByTagName("input")
    for (i in 0 until checkboxes.length) {
        val checkbox = checkboxes[i] as HTMLInputElement
        if (teachers.any { it == checkbox.name }) {
            val check = teachers.map { document.querySelector("input[name='$it']") as HTMLInputElement }
            check .mapIndexed { index, box ->
                box.checked = checked
                checkList[index] = checked
            }
        }
        }

}

val teacherChoose = FC<Props> {
    val param = useParams()["file"]!!
    var actions by useState(TypeOfAction.AddTeacher)

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachersNames").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.schedule + "namesFromExcel/${param}")
        })

    val teacherInDBQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachers").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.teachers + "allNames")
        })



    val queryClient = useQueryClient()

    val updateMutation = useMutation<HTTPResult, Any, AddTeachersToDatabase, Any>(
        mutationFn = { list ->
            fetch(
                Config.schedule + "loadSchedule/${param}",
                jso {
                    method = "POST"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    body = Json.encodeToString(list)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(arrayOf("teacherLessons").unsafeCast<QueryKey>())
                queryClient.invalidateQueries<Any>(arrayOf("teachers").unsafeCast<QueryKey>())
            }
        }
    )


    if (query.isLoading) {
        div {
            ReactHTML.style {
                +Styles.animation
            }

            className = ClassName("spin-wrapper")
            div {
                className = ClassName("spinner")

            }
        }
    } else if (query.isError) {
        div {
            +"Error"
        }
    } else {
        val data = Json.decodeFromString<List<String>>(query.data ?: "")
        val teachersInDB = try {
            Json.decodeFromString<List<String>>(teacherInDBQuery.data ?: "")
        }catch (e: Exception){
            emptyList()
        }
        val checkList = data.map{false}.toMutableList()

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
                +"Загрузка расписания"
            }
        }
        ReactHTML.div {
            ReactHTML.label {
                css {
                    fontSize = 20.px
                    border = None.none
                    borderRadius = 30.px
                }
                +"Выберите преподавателя(-ей), которого(-ых) вы хотите загрузить:"
            }
            label{
                style{
                    +Styles.labStyle
                }
                id = "podskazka"
                className = ClassName("tooltip")
                +"*"
            }
        }

        span {
            TypeOfAction.values().map { typeAction ->
                input {
                    type = InputType.radio
                    name = "typeOfAction"
                    value = it
                    id = typeAction.name
                    if (typeAction == actions){
                        checked = true
                    }
                    onClick = {
                        actions = typeAction
                    }
                }
                label {
                    form = typeAction.name
                    + typeAction.nameOfAction
                }
            }
        }
        form{
      div{
          css{
              padding = 4.px
          }
         // ReactHTML.style {
            //  +Styles.checkboxStyle
         // }
          input{
              type = InputType.checkbox
              name = "Все преподаватели"
              value = data
              onChange = { event ->
                  val check = event.target
                  selectAll(data, check.checked, checkList)
              }
          }
          label{
              +"Все преподаватели"
          }
      }
 data.mapIndexed { index,teachers->
  div {
      css{
          padding = 4.px
      }
      input {
          type = InputType.checkbox
          name = teachers
          value = teachers
          onChange = {
              checkList[index] = !checkList[index]
          }
      }
      label {
          if (teachersInDB.contains(teachers))
              css {
                  //backgroundColor = NamedColor.black
                //  textShadow = TextShadow(1.px, 1.px,1.px, NamedColor.red)
                  boxSizing = BoxSizing.borderBox
                  border = Border(2.px, LineStyle.solid, NamedColor.red)
                  boxShadow = BoxShadow(1.px,1.px,1.px, NamedColor.red)
                  //border = 2.px
                  //borderColor = NamedColor.red

              }
          +teachers
      }
      }
  }
        }

        form{
            action = "/"
            button{
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
                onClick = {
                    val teach = data.mapIndexed { index, s ->
                        if(checkList[index]){
                            s
                        }else{
                            null
                        }
                    }.filterNotNull()
                    updateMutation.mutateAsync(AddTeachersToDatabase(teach, actions),null)
                }
            }
        }}

    CArrowL{
        path = "/#/home"
    }
    CFooter{}
}

