package io.github.lordraydenmk.errorhandling.data

import arrow.core.Either

interface SignUp {

    //@POST("/signup")
    suspend fun signUp(body: SignUpBody): Either<SignUpErrorDto, SignUpResult>
}