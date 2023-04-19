package com.kettl

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.kettl.auth.configureAuthentication
import com.kettl.injection.configureKoin
import com.kettl.serialization.JavaTimeModule
import com.kettl.serialization.ModelsModule
import com.kettl.web.configureRouting
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureKoin()
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
            })
            registerModules(
                JavaTimeModule(),
                ModelsModule()
            )
        }
    }
    configureAuthentication()
    configureRouting()
}