import classes.Teacher
import classes.TimeTable
import components.CTable
import components.reader
import js.core.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
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
import tanstack.react.query.useQuery
import tools.fetchText
import web.dom.document

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

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
            }
        }
    }
}

val teacherChoose = FC<Props> {
    val param = useParams()["file"]!!

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachersNames").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.schedule + "namesFromExcel/${param}")
        })

    if (query.isSuccess) {
        val data = Json.decodeFromString<List<Teacher>>(query.data ?: "")

        select {
            data.map {
                option {
                    +it.fullName
                }
            }
        }
    }
}

val container = FC<Props> {
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
        val lessons = Json.decodeFromString<TimeTable>(query.data ?: "")

        CTable {
            table = lessons
        }
    }
}







