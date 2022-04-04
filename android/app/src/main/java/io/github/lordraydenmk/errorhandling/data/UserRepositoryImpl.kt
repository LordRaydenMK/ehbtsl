package io.github.lordraydenmk.errorhandling.data

import arrow.core.Either
import arrow.core.Nel
import arrow.core.computations.either
import arrow.core.left
import io.github.lordraydenmk.errorhandling.domain.FormFieldError
import io.github.lordraydenmk.errorhandling.domain.FormFieldName
import io.github.lordraydenmk.errorhandling.domain.SignUpData
import io.github.lordraydenmk.errorhandling.domain.SignUpError
import io.github.lordraydenmk.errorhandling.domain.Token
import io.github.lordraydenmk.errorhandling.domain.UserRepository
import java.io.IOException

class UserRepositoryImpl(private val signUp: SignUp = FakeSignUp()) : UserRepository {

    override suspend fun doSignUp(
        signUpData: SignUpData
    ): Either<SignUpError, Token> = either {
        val body = signUpData.toBody()
        val result = try {
            signUp.signUp(body).mapLeft { it.toDomainError() }
        } catch (t: IOException) {
            SignUpError.IOError(t).left()
        }.bind()
        Token(result.token)
    }

    private fun SignUpData.toBody(): SignUpBody = SignUpBody(name, email, phoneNumber)

    private fun SignUpErrorDto.toDomainError(): SignUpError =
        if (errors.isEmpty()) SignUpError.HttpError(message)
        else {
            val errorsNel = errors.mapNotNull { it.toDomainError() }
            SignUpError.ValidationError(Nel.fromListUnsafe(errorsNel))
        }

    private fun FormFieldDto.toDomainError(): FormFieldError? {
        val formField = when (field) {
            "name" -> FormFieldName.NAME
            "email" -> FormFieldName.EMAIL
            "phoneNumber" -> FormFieldName.PHONE_NUMBER
            else -> null // ignore errors for fields we don't know
        }
        return formField?.let { FormFieldError(it, Nel.fromListUnsafe(errors)) }
    }
}