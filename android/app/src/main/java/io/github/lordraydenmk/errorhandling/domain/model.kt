package io.github.lordraydenmk.errorhandling.domain

import arrow.core.*
import java.io.IOException

typealias ValidationRes<A> = ValidatedNel<FormFieldError, A>

fun validateName(name: String): ValidationRes<String> =
    if (name.isNotBlank()) name.valid()
    else FormFieldError(
        FormField.NAME,
        "Name can't be blank".nel()
    ).invalidNel()

data class SignUpData private constructor(val name: String, val signUpId: SignUpId) {

    val email: String?
        get() = when (signUpId) {
            is SignUpId.Email -> signUpId.value
            is SignUpId.PhoneNumber -> null
        }

    val phoneNumber: String?
        get() = when (signUpId) {
            is SignUpId.Email -> null
            is SignUpId.PhoneNumber -> signUpId.value
        }

    companion object {

        fun create(name: String, email: String?, phoneNumber: String?): ValidationRes<SignUpData> =
            validateName(name).zip(SignUpId.create(email, phoneNumber), ::SignUpData)
    }
}

sealed class SignUpId {
    data class Email private constructor(val value: String) : SignUpId()
    data class PhoneNumber private constructor(val value: String) : SignUpId()

    companion object {
        fun create(email: String?, phoneNumber: String?): ValidationRes<SignUpId> = TODO()
    }
}

@JvmInline
value class Token(val value: String)

enum class FormField {
    NAME, EMAIL, PHONE_NUMBER
}

data class FormFieldError(val formField: FormField, val errors: Nel<String>)

sealed class SignUpError {
    data class ValidationError(val errors: Nel<FormFieldError>) : SignUpError()
    data class HttpError(val message: String) : SignUpError()
    data class IOError(val e: IOException) : SignUpError()
}
