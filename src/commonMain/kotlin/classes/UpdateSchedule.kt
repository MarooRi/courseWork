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
@Serializable
enum class TypeOfWeek {
    upWeek, lowWeek
}

@Serializable
enum class TypeOfAction(val nameOfAction: String) {
    AddTeacher("Добавить преподавателя"), SumSchedule("Cоединить расписание"), ReplaceSchedule("Поменять расписание")
}

enum class TypeOfButton {
    Up, Down, Left, Right
}
@Serializable
class GroupTimeTable(
    val upWeek: List<UnwindLesson>,
    val lowWeek: List<UnwindLesson>
)
@Serializable
class UnwindLesson(
    val teacher: String,
    val dayOfWeek: String,
    val classes: Lesson
)