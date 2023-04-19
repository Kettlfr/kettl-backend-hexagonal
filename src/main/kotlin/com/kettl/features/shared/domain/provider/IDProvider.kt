package com.kettl.features.shared.domain.provider

import com.kettl.features.shared.domain.Snowflake

interface IDProvider {
    fun generate(): Snowflake
}