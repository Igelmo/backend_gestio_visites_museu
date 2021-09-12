package fib.museu.domain.datamodels

import kotlinx.serialization.Serializable

@Serializable
data class GuideObject(
    val guideEmail: String,
    val person: PersonObject,
    val workedHours: Int,
    val username: String,
    val password: String,
)
