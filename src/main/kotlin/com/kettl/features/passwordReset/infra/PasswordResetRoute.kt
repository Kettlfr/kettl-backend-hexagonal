package com.kettl.features.passwordReset.infra

import com.kettl.auth.SessionAuth
import com.kettl.ext.respondResult
import com.kettl.features.passwordReset.app.PasswordResetController
import com.kettl.features.user.app.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.passwordResetRoute() {
    val passwordResetController: PasswordResetController by inject()

    route("/users") {
        route("account") {
            post("/reset-password") {
                val response = passwordResetController.askResetPasswordToken(call)

                call.respondResult(response)
            }
            post("/reset-password-confirm") {
                val response = passwordResetController.confirmResetPassword(call)

                call.respondResult(response)
            }
        }
    }
}