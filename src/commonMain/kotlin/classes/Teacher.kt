package classes

import classes.TimeTable
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val fullName: String,
    var table: TimeTable
)