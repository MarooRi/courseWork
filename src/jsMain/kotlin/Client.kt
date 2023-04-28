import classes.*
import components.CButtons
import components.CTable
import components.reader
import csstype.*
import emotion.react.css
import js.core.Record
import js.core.get
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.*
import react.dom.client.createRoot
import react.dom.html.ButtonType
import react.dom.html.FormMethod
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.legend
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.style
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import react.router.Params
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
import web.html.InputType
import kotlin.js.json

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val app = FC<Props>("App") {
    HashRouter {
        QueryClientProvider {
            client = QueryClient()
            //container { }

            ReactQueryDevtools { }

            Routes {
                Route {
                    path = "/home/:file"
                    element = container.create {}
                }
                Route {
                    path = "/"
                    element =  reader.create()
                }
            }
        }
    }
}

val container = FC<Props> {
    val param = useParams()["file"]

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







