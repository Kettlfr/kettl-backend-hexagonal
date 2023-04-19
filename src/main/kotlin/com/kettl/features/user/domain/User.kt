package com.kettl.features.user.domain

import com.kettl.features.shared.domain.Snowflake
import java.time.Instant

data class User(
    val id: Snowflake = Snowflake(0),
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val emailVerified: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val hashedPassword: String = "",
)