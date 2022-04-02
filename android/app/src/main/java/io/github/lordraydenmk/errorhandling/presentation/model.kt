package io.github.lordraydenmk.errorhandling.presentation

enum class IdType { EMAIL, PHONE }

data class FormField(val value: String, val error: String? = null)

data class ViewState(
    val showProgress: Boolean,
    val error: String?,
    val idType: IdType,
    val name: FormField,
    val id: FormField,
) {

    val nameLabel: String = "Name"

    val idLabel: String
        get() = when (idType) {
            IdType.EMAIL -> "Email"
            IdType.PHONE -> "Phone number"
        }

    fun withError(nameError: String?, idError: String?): ViewState =
        copy(name = name.copy(error = nameError), id = id.copy(error = idError))

    companion object {

        val DEFAULT = ViewState(false, null, IdType.EMAIL, FormField(""), FormField(""))
    }
}