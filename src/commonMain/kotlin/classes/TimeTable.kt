package classes

import kotlinx.serialization.Serializable

@Serializable
data class TimeTable(
    val upWeek: List<Day>,
    val lowWeek: List<Day>
)