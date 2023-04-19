package com.kettl.web

import com.kettl.features.passwordReset.infra.passwordResetRoute
import com.kettl.features.user.infra.userRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        get("/") {
/*                call.respond(
                   *//* HttpResponse.ok(
                        mapOf(
                            "name" to "Kettl API",
                            "version" to Config.API_VERSION,
                            "description" to "Welcome to the Kettl API",
                            "documentation_url" to "https://developers.kettl.fr"
                        )
                    )*//*
                )*/
        }
        userRoute()
        passwordResetRoute()
        //organizationRoute()
    }
}