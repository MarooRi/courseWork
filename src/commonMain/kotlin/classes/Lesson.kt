package classes

import kotlinx.serialization.Serializable

@Serializable
data class Lesson (
    val group: String = "",
    val classRoom: String = "",
    val name: String = "",
)