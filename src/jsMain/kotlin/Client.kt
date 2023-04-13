
import classes.Teacher
import classes.TimeTable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.FormMethod
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.router.dom.HashRouter
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
        QueryClientProvider {
            client = QueryClient()
            container{ }
            ReactQueryDevtools { }
        }
}

val container = FC<Props> {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("1").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.teachers)
        }
    )

    div {
        + "Hello!"
    }
    if (query.isLoading) {
        div {
            +" l o a d i n g . . . \uD83D\uDD01"
        }
    }else {
        if (query.isError){
            div {
                +" e r r o r . . . ❌"
            }
        }else {

            val data = Json.decodeFromString<List<Teacher>>(query.data ?: "")
            ol {
                data.map {
                    li {
                        +it.fullName
                    }
                }
            }
        }
    }
}