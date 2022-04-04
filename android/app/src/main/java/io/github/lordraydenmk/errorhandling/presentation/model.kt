package io.github.lordraydenmk.errorhandling.presentation

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

    fun withError(nameError: String?, idError: String?): ViewState =
        copy(name = name.copy(error = nameError), id = id.copy(error = idError))
}