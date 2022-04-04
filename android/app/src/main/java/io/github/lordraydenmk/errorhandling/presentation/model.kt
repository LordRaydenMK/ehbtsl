package io.github.lordraydenmk.errorhandling.presentation

import arrow.core.Nel
import io.github.lordraydenmk.errorhandling.domain.FormFieldError
import io.github.lordraydenmk.errorhandling.domain.FormFieldName
import io.github.lordraydenmk.errorhandling.domain.SignUpError

enum class IdType { EMAIL, PHONE }

data class FormField(val value: String, val error: String? = null)

data class ViewState(
    val showProgress: Boolean = false,
    val error: String? = null,
    val idType: IdType = IdType.EMAIL,
    val name: FormField = FormField(""),
    val id: FormField = FormField(""),
) {

    val nameLabel: String = "Name"

    val idLabel: String
        get() = when (idType) {
            IdType.EMAIL -> "Email"
            IdType.PHONE -> "Phone number"
        }

    val switchButtonLabel: String
        get() = when (idType) {
            IdType.EMAIL -> "Use phone number"
            IdType.PHONE -> "Use email"
        }

    fun withErrors(nameError: String?, idError: String?): ViewState =
        copy(name = name.copy(error = nameError), id = id.copy(error = idError))

    fun withErrors(errors: Nel<FormFieldError>): ViewState {
        val nameError = errors.firstOrNull { it.formFieldName == FormFieldName.NAME }?.errors?.head
        val idField =
            if (idType == IdType.EMAIL) FormFieldName.EMAIL else FormFieldName.PHONE_NUMBER
        val idError = errors.firstOrNull { it.formFieldName == idField }?.errors?.head
        return withErrors(nameError, idError)
    }

    fun withError(error: SignUpError): ViewState =
        when (error) {
            is SignUpError.HttpError -> copy(showProgress = false, error = error.message)
            is SignUpError.IOError ->
                copy(showProgress = false, error = "Problem connecting to server")
            is SignUpError.ValidationError -> withErrors(error.errors)
        }

    fun clearErrors(): ViewState = withErrors(null, null)
}