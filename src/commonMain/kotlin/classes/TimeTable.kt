package classes

import kotlinx.serialization.Serializable

@Serializable
data class TimeTable(
    var upWeek: List<Day>,
    var lowWeek: List<Day>
)