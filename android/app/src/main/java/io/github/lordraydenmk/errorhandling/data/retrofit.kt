package io.github.lordraydenmk.errorhandling.data

import arrow.core.Either
import arrow.core.left
import kotlinx.coroutines.delay
import retrofit2.http.POST

interface SignUp {

    @POST("/signup")
    suspend fun signUp(body: SignUpBody): Either<SignUpErrorDto, SignUpResult>
}

class FakeSignUp : SignUp {

    override suspend fun signUp(body: SignUpBody): Either<SignUpErrorDto, SignUpResult> {
        delay(3_000)
        return SignUpErrorDto("Something Went wrong!", emptyList()).left()
    }
}