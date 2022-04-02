package io.github.lordraydenmk.errorhandling.data

import kotlinx.serialization.Serializable

@Serializable
data class SignUpBody(
    val name: String,
    val email: String?,
    val phoneNumber: String?,
)

@Serializable
data class SignUpResult(val token: String)

@Serializable
data class FormFieldDto(val field: String, val errors: List<String>)

@Serializable
data class SignUpErrorDto(
    val message: String,
    val errors: List<FormFieldDto>
)