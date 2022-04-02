package io.github.lordraydenmk.errorhandling.domain

import arrow.core.Either

interface UserRepository {

    suspend fun doSignUp(signUpData: SignUpData): Either<SignUpError, Token>
}