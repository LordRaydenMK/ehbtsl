package io.github.lordraydenmk.errorhandling.presentation

enum class IdType { EMAIL, PHONE }

data class FormField(val value: String, val error: String? = null)

data class ViewState(
    val showProgress: Boolean,
    val idType: IdType,
    val name: FormField,
    val id: FormField,
) {

    fun withError(nameError: String?, idError: String?): ViewState =
        copy(name = name.copy(error = nameError), id = id.copy(error = idError))

    companion object {

        val DEFAULT = ViewState(false, IdType.EMAIL, FormField(""), FormField(""))
    }
}