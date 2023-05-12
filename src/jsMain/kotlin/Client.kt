import classes.TimeTable
import classes.TypeOfWeek
import components.table.CTable
import components.groups.CGroupContainer
import components.reader
import components.table.CMode
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
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.useParams
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
                    path = "/1"
                    element = reader.create()
                }
                Route {
                    path = "/312"
                    element = CGroupContainer.create()
                }
                Route {
                    path = "/"
                    element = container.create()
                }
            }
        }
    }
}

val teacherChoose = FC<Props> {
    val param = useParams()["file"]!!
    val queryClient = useQueryClient()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachersNames").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.schedule + "namesFromExcel/${param}")
        })

    val updateMutation = useMutation<HTTPResult, Any, List<String>, Any>(
        mutationFn = { list ->
            fetch(
                Config.schedule + "loadSchedule/" + param,
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
            }
        }
    )

    if (query.isSuccess) {
        val data = Json.decodeFromString<List<String>>(query.data ?: "")

        select {
            data.map {
                option {
                    +it
                }
            }
        }
        button{
            +"submit"
            onClick = {
                updateMutation.mutateAsync(data, null)
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
            +"Loading"
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
        typeOfWeek.Provider(mode){
            CTable {
                table = lessons
            }
        }
    }
}







