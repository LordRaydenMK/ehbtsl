package io.github.lordraydenmk.errorhandling.data

//@Serializable
data class SignUpBody(
    val name: String,
    val email: String?,
    val phoneNumber: String?,
)

//@Serializable
data class SignUpResult(val token: String)

data class FormFieldDto(val field: String, val errors: List<String>)

data class SignUpErrorDto(
    val message: String,
    val errors: List<FormFieldDto>
)