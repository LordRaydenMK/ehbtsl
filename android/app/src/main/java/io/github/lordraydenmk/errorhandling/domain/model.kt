package io.github.lordraydenmk.errorhandling.domain

import arrow.core.Nel
import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.nel
import arrow.core.valid
import arrow.core.zip
import java.io.IOException

typealias ValidationRes<A> = ValidatedNel<String, A>

typealias FormValidationRes<A> = ValidatedNel<FormFieldError, A>

fun validateName(name: String): ValidationRes<String> =
    if (name.isNotBlank()) name.valid()
    else "Name can't be blank".invalidNel()

sealed interface SignUpId

@JvmInline
value class Email private constructor(val value: String) : SignUpId {

    companion object {

        fun create(value: String): ValidationRes<Email> =
            if (value.contains('@')) Email(value).valid()
            else "Email must contain '@'".invalidNel()
    }
}

@JvmInline
value class PhoneNumber private constructor(val value: String) : SignUpId {

    companion object {

        fun create(value: String): ValidationRes<PhoneNumber> =
            validateStart(value).zip(validateLength(value)) { _, phone ->
                PhoneNumber(phone)
            }

        private fun validateStart(value: String): ValidationRes<String> =
            if (value.startsWith('+')) value.valid()
            else "Phone number must start with '+'".invalidNel()

        private fun validateLength(value: String): ValidationRes<String> =
            if (value.length > 4) value.valid()
            else "Phone number must be at least 4 chars".invalidNel()
    }
}

data class SignUpData(val name: String, val signUpId: SignUpId) {

    val email: String?
        get() = when (signUpId) {
            is Email -> signUpId.value
            is PhoneNumber -> null
        }

    val phoneNumber: String?
        get() = when (signUpId) {
            is Email -> null
            is PhoneNumber -> signUpId.value
        }

    companion object {

        fun createEmail(name: String, email: String): FormValidationRes<SignUpData> {
            val validatedName = validateName(name)
                .mapLeft { FormFieldError(FormFieldName.NAME, it).nel() }
            val validatedId = Email.create(email)
                .mapLeft { FormFieldError(FormFieldName.EMAIL, it).nel() }
            return validatedName.zip(validatedId, ::SignUpData)
        }

        fun createPhone(name: String, phoneNumber: String): FormValidationRes<SignUpData> {
            val validatedName = validateName(name)
                .mapLeft { FormFieldError(FormFieldName.NAME, it).nel() }
            val validatedId = PhoneNumber.create(phoneNumber)
                .mapLeft { FormFieldError(FormFieldName.PHONE_NUMBER, it).nel() }
            return validatedName.zip(validatedId, ::SignUpData)
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
