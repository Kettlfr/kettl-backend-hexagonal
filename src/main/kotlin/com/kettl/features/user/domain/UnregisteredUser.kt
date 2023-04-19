package com.kettl.features.user.domain

import com.kettl.features.shared.domain.Snowflake
import java.time.Instant

data class UnregisteredUser(
     val email: String = "",
     val firstName: String = "",
     val lastName: String = "",
    val clearPassword: String = "",
)