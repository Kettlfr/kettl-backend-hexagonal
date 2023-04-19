package com.kettl.features.passwordReset.app

import com.kettl.features.passwordReset.domain.ConfirmPasswordResetUseCase
import com.kettl.features.passwordReset.domain.PasswordResetUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import com.kettl.features.shared.domain.Result


class PasswordResetController(
    private val resetPasswordUseCase: PasswordResetUseCase,
    private val confirmResetPasswordUseCase: ConfirmPasswordResetUseCase,
) {

    suspend fun askResetPasswordToken(call: ApplicationCall) : Result<String> {
        val post = call.receiveParameters()
        val email = post["email"] ?: ""

        return this.resetPasswordUseCase.execute(PasswordResetUseCase.Input(email))
    }

    suspend fun confirmResetPassword(call: ApplicationCall) : Result<String> {
        val post = call.receiveParameters()
        val token = post["token"] ?: ""
        val newPassword = post["newPassword"] ?: ""

        return this.confirmResetPasswordUseCase.execute(ConfirmPasswordResetUseCase.Input(token, newPassword))
    }
}