package fib.museu.domain.datamodels

import kotlinx.serialization.Serializable

@Serializable
data class PersonObject (
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
)
