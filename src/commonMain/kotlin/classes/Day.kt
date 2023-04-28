package classes

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val dayOfWeek: String, //название дня
    var classes: List<Lesson> //пары в этот день
)