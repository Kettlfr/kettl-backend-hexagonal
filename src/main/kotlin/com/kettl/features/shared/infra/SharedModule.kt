package com.kettl.features.shared.infra


import com.kettl.config.Config
import com.kettl.features.shared.domain.provider.HasherProvider
import com.kettl.features.shared.domain.provider.IDProvider
import com.kettl.features.shared.domain.provider.CharSequenceProvider
import com.kettl.features.shared.infra.adapters.ArgonHasherProvider
import com.kettl.features.shared.infra.adapters.SecureRandomCharSequenceProvider
import com.kettl.features.shared.infra.adapters.ShaHasherProvider
import com.kettl.features.shared.infra.adapters.SnowflakeIDProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedModule = module {
    single<IDProvider> {
        SnowflakeIDProvider(Config.NODE_ID)
    }
    single<HasherProvider>(named("argon")) {
        ArgonHasherProvider()
    }
    single<HasherProvider>(named("sha512")) {
        ShaHasherProvider()
    }
    single<CharSequenceProvider> {
        SecureRandomCharSequenceProvider()
    }
}