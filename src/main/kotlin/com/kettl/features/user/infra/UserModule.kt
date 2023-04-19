package com.kettl.features.user.infra

import com.kettl.config.Config
import com.kettl.features.user.app.UserController
import com.kettl.features.user.domain.UserSignupUseCase
import com.kettl.features.user.domain.ports.UserRepository
import com.kettl.features.user.infra.adapters.UserScyllaRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userModule = module {
    single {
        UserController(get())
    }
    single {
        UserSignupUseCase(get(), get(named("argon")), get())
    }
    single<UserRepository> {
        UserScyllaRepository()
    }
}