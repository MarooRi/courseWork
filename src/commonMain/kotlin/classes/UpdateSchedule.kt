package classes

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class UpdateSchedule(
    val typeOfWeek: TypeOfWeek,
    val teacherName: String,
    val week: List<Day>
){
    val path = "updateLesson"
}

val UpdateSchedule.json
    get() = Json.encodeToString(this)

enum class TypeOfWeek {
    upWeek, lowWeek
}

enum class TypeOfButton {
    Up, Down, Left, Right
}
