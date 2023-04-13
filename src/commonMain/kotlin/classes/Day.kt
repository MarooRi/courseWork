package classes

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val dayOfWeek: String, //название дня
    val classes: List<Lesson> //пары в этот день
)