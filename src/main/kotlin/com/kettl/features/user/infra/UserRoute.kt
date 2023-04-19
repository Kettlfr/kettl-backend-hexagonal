package com.kettl.features.user.infra;

import com.kettl.auth.SessionAuth.Companion.SESSION_AUTHENTICATION
import com.kettl.ext.respondResult
import com.kettl.features.user.app.UserController

import com.kettl.util.ResultUtil
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.userRoute() {
    val userController: UserController by inject()

    route("/users") {

        post {
            val response = userController.signup(call)

            call.respondResult(response)
        }

        route("/session") {
            post {
                val response = userController.login(call)

                call.respond(response)
            }

            authenticate(SESSION_AUTHENTICATION) {
                delete {
                    val response = userController.logout(call)

                    call.respond(response)
                }
            }
        }

        route("account") {
            authenticate(SESSION_AUTHENTICATION) {
                put("/password") {
                    
                }
            }
            post("/reset-password") {

            }
            post("/reset-password-confirm") {

            }
        }
    }
}