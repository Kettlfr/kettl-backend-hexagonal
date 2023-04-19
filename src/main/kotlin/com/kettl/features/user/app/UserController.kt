package com.kettl.features.user.app

import com.kettl.auth.UserSession
import com.kettl.features.user.domain.UserSignupUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import com.kettl.features.shared.domain.Result
import com.kettl.features.user.domain.UnregisteredUser
import com.kettl.features.user.domain.User
import io.ktor.server.sessions.*

class UserController(private val userSignupUseCase: UserSignupUseCase) {

    suspend fun signup(call: ApplicationCall) : Result<User> {
        val post = call.receiveParameters()
        val email = post["email"] ?: ""
        val password = post["password"] ?: ""
        val firstName = post["firstName"] ?: ""
        val lastName = post["lastName"] ?: ""

        return this.userSignupUseCase.execute(UserSignupUseCase.Input(UnregisteredUser(
            email = email,
            firstName = firstName,
            lastName = lastName,
            clearPassword = password,
        )))
    }

    suspend fun login(call: ApplicationCall) : Result<User> {
        val post = call.receiveParameters()
        val email = post["email"] ?: ""
        val password = post["password"] ?: ""

        when {
            email.isEmpty() -> return Result.failure("auth-login-0001", "Email is required", "Ensure that the email is not empty")
            password.isEmpty() -> return Result.failure("auth-login-0002", "Password is required", "Ensure that the password is not empty")
        }
        // TODO: Implement login
        return this.userSignupUseCase.execute(UserSignupUseCase.Input(UnregisteredUser(email, password, "", "")))
    }

    suspend fun logout(call: ApplicationCall) : Result<Boolean> {
        call.sessions.clear<UserSession>()
        return Result.success(true)
    }
}