package classes

import kotlinx.serialization.Serializable

@Serializable
data class Lesson (
    val group: List<String> = emptyList(),
    val classRoom: String = "",
    val name: String = "",
)