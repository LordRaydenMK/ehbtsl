package io.github.lordraydenmk.errorhandling.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.computations.either
import io.github.lordraydenmk.errorhandling.data.UserRepositoryImpl
import io.github.lordraydenmk.errorhandling.domain.SignUpData
import io.github.lordraydenmk.errorhandling.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    val state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())

    fun onSubmit(name: String, id: String) {
        viewModelScope.launch {
            signUpUser(name, id, state.value)
                .fold({ state.value = it }, { /* TODO: navigate to Home */ })
        }
    }

    fun onSwitchId(currentIdType: IdType) {
        val newIdType = when (currentIdType) {
            IdType.EMAIL -> IdType.PHONE
            IdType.PHONE -> IdType.EMAIL
        }
        state.update { it.copy(idType = newIdType) }
    }

    private suspend fun signUpUser(
        name: String,
        id: String,
        viewState: ViewState
    ): Either<ViewState, Unit> = either {
        state.update { it.clearErrors().copy(showProgress = true) }
        val signUpData = validateForm(name, id, viewState).bind()
        userRepository.doSignUp(signUpData)
            .mapLeft { viewState.withError(it) }
            .bind()
        Unit
    }

    private fun validateForm(
        name: String,
        id: String,
        viewState: ViewState
    ): Either<ViewState, SignUpData> {
        val idType = viewState.idType
        val email = if (idType == IdType.EMAIL) id else null
        val phone = if (idType == IdType.PHONE) id else null
        val signUpData = SignUpData.create(name, email, phone)
            .mapLeft { viewState.withErrors(it) }
        return signUpData.toEither()
    }
}