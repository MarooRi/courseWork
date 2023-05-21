package components.edit

import Config
import classes.TimeTable
import classes.TypeOfWeek
import components.CFooter
import components.table.CMode
import csstype.ClassName
import csstype.FontFamily
import csstype.TextAlign
import csstype.px
import emotion.react.css
import js.core.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import queryError.QueryError
import react.FC
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.style
import react.router.useParams
import react.useState
import styles.CArrowL
import styles.Styles
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import typeOfWeek


val container = FC<Table> { props->
    val (mode, setMode) = useState(TypeOfWeek.upWeek)
    val param = useParams()["teacher"]!!
        val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teacherLessons").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.schedule + param)
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
    CArrowL{
        path = "/#/editOpt"
    }
    CFooter{}
}
