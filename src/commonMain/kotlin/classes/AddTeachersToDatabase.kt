package classes

import kotlinx.serialization.Serializable

@Serializable
class AddTeachersToDatabase (
    val teachers: List<String>,
    val typeOfAction: TypeOfAction
)