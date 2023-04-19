package com.kettl.injection

import com.kettl.database.scylla.ScyllaGateway
import com.kettl.features.passwordReset.infra.passwordResetModule
import com.kettl.features.shared.infra.sharedModule
import com.kettl.features.user.infra.userModule
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

class AppModule {
    companion object {
        val appModule = module {
            single {
                ScyllaGateway()
            }
        }
    }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(userModule, sharedModule, AppModule.appModule, passwordResetModule)
    }
}
