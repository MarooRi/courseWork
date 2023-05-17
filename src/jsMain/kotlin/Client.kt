import classes.AddTeachersToDatabase
import classes.TimeTable
import classes.TypeOfAction
import classes.TypeOfWeek
import components.groups.CGroupContainer
import components.reader
import components.table.CMode
import components.table.CTable
import csstype.ClassName
import csstype.Color
import emotion.react.css
import js.core.get
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.*
import react.dom.client.createRoot
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.style
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.useParams
import styles.Styles
import tanstack.query.core.QueryClient
import tanstack.query.core.QueryKey
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import web.dom.document
import web.html.InputType
import kotlin.js.json

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val typeOfWeek = createContext(TypeOfWeek.upWeek)

val app = FC<Props>("App") {
    HashRouter {
        QueryClientProvider {
            client = QueryClient()
            ReactQueryDevtools { }

            Routes {
                Route {
                    path = "/home/:file"
                    element = teacherChoose.create {}
                }
                Route {
                    path = "/"
                    element = reader.create()
                }
                Route {
                    path = "/312"
                    element = CGroupContainer.create()
                }
                Route {
                    path = "/222"
                    element = container.create()
                }
            }
        }
    }
}

val teacherChoose = FC<Props> {
    val param = useParams()["file"]!!
    val queryClient = useQueryClient()
    var action by useState(TypeOfAction.AddTeacher)

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

    val updateMutation = useMutation<HTTPResult, Any, AddTeachersToDatabase, Any>(
        mutationFn = { command ->
            fetch(
                Config.schedule + "loadSchedule/" + param,
                jso {
                    method = "POST"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    body = Json.encodeToString(command)
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

    if (query.isSuccess && teacherInDBQuery.isSuccess) {
        val data = Json.decodeFromString<List<String>>(query.data ?: "")
        val teachersInDB = try {
            Json.decodeFromString<List<String>>(teacherInDBQuery.data ?: "")
        }catch (e: Exception){
            emptyList()
        }

        select {
            data.map {
                option {
                    if (teachersInDB.contains(it))
                        css {
                            backgroundColor = Color("#FF0000")
                        }
                    +it
                }
            }
        }
        span {
           TypeOfAction.values().map { typeAction ->
               input {
                   type = InputType.radio
                   name = "typeOfAction"
                   value = it
                   id = typeAction.name
                   if (typeAction == action){
                       checked = true
                   }
                   onClick = {
                       action = typeAction
                   }
               }
               label {
                   form = typeAction.name
                   + typeAction.nameOfAction
               }
           }
        }

        button {
            +"submit"
            onClick = {
                updateMutation.mutateAsync(AddTeachersToDatabase(listOf("доц. Каштанов А.Л."), action), null)
            }
        }
    }
}

val container = FC<Props> {
    val (mode, setMode) = useState(TypeOfWeek.upWeek)

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teacherLessons").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.schedule + "доц. Альтман Е.А.")
        })

    if (query.isLoading) {
        div {
            style {
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
        CMode {
            _mode = mode
            _setMode = setMode
        }

        val lessons = Json.decodeFromString<TimeTable>(query.data ?: "")
        typeOfWeek.Provider(mode) {
            CTable {
                table = lessons
            }
        }
    }
}







