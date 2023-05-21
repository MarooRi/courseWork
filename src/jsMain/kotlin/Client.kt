import classes.TypeOfWeek
import react.create
import react.createContext
import react.dom.client.createRoot
import web.dom.document

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val typeOfWeek = createContext(TypeOfWeek.upWeek)





