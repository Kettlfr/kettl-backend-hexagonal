package com.kettl.features.passwordReset.domain

import com.kettl.features.shared.domain.Snowflake

data class PasswordReset(
    val hashedToken: String = "",
    val userId: Snowflake = Snowflake(0),
)