package com.kettl.features.passwordReset.infra

import com.kettl.features.passwordReset.app.PasswordResetController
import com.kettl.features.passwordReset.domain.ConfirmPasswordResetUseCase
import com.kettl.features.passwordReset.domain.PasswordResetUseCase
import com.kettl.features.passwordReset.domain.ports.PasswordResetRepository
import com.kettl.features.passwordReset.infra.adapters.PasswordResetScyllaRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val passwordResetModule = module {
    single {
        PasswordResetController(get(), get())
    }
    single {
        PasswordResetUseCase(get(), get(), get(named("sha512")), get())
    }
    single {
        ConfirmPasswordResetUseCase(get(), get(named("sha512")), get())
    }
    single<PasswordResetRepository> {
        PasswordResetScyllaRepository()
    }
}