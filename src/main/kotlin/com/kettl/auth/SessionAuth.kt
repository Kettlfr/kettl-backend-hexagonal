package com.kettl.auth

import com.kettl.auth.SessionAuth.Companion.SESSION_AUTHENTICATION
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class SessionAuth {
    companion object {
        const val SESSION_AUTHENTICATION = "session-auth"
    }
}

fun Application.configureAuthentication() {
    install(Sessions) {
        //val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserSession>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 24 * 7
            //cookie.secure = true
            //transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
        }
    }
    install(Authentication) {
        session<UserSession>(SESSION_AUTHENTICATION) {
            validate { session ->
                if (session.email.isEmpty()) {
                    null
                } else {
                    UserIdPrincipal(session.email)
                }
            }
            challenge {
                if (it == null) {
                    return@challenge call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                } else if (it.emailVerified) {
                    return@challenge call.respondRedirect("/users/me")
                } else {
                    return@challenge call.respondRedirect("/users/me")
                }
            }
        }
    }
}