package io.github.lordraydenmk.errorhandling.domain

import arrow.core.Nel
import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.nel
import arrow.core.valid
import arrow.core.zip
import java.io.IOException

typealias ValidationRes<A> = ValidatedNel<FormFieldError, A>

fun validateName(name: String): ValidationRes<String> =
    if (name.isNotBlank()) name.valid()
    else FormFieldError(FormFieldName.NAME, "Name can't be blank".nel()).invalidNel()

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

        fun create(name: String, email: String?, phoneNumber: String?): ValidationRes<SignUpData> {
            val validatedName = validateName(name)
            val validatedId = SignUpId.create(email, phoneNumber)
            return validatedName.zip(validatedId, ::SignUpData)
        }
    }
}

sealed class SignUpId {
    data class Email private constructor(val value: String) : SignUpId() {
        companion object {
            fun create(email: String): ValidationRes<Email> =
                if (email.contains("@")) Email(email).valid()
                else FormFieldError(
                    FormFieldName.EMAIL,
                    "Email must contain '@'".nel()
                ).invalidNel()
        }
    }

    data class PhoneNumber private constructor(val value: String) : SignUpId() {
        companion object {
            fun create(phoneNumber: String): ValidationRes<PhoneNumber> =
                when {
                    !phoneNumber.startsWith("+") -> FormFieldError(
                        FormFieldName.PHONE_NUMBER,
                        "Must start with '+'".nel()
                    ).invalidNel()
                    phoneNumber.length < 4 -> FormFieldError(
                        FormFieldName.PHONE_NUMBER,
                        "Must be at least 4 characters".nel()
                    ).invalidNel()
                    else -> PhoneNumber(phoneNumber).valid()
                }
        }
    }

    companion object {

        fun create(email: String?, phoneNumber: String?): ValidationRes<SignUpId> =
            when {
                email != null -> Email.create(email)
                phoneNumber != null -> PhoneNumber.create(phoneNumber)
                else -> FormFieldError(FormFieldName.EMAIL, "Must provide an ID".nel()).invalidNel()
            }
    }
}

@JvmInline
value class Token(val value: String)

enum class FormFieldName {
    NAME, EMAIL, PHONE_NUMBER
}

data class FormFieldError(val formFieldName: FormFieldName, val errors: Nel<String>)

sealed class SignUpError {
    data class ValidationError(val errors: Nel<FormFieldError>) : SignUpError()
    data class HttpError(val message: String) : SignUpError()
    data class IOError(val e: IOException) : SignUpError()
}
