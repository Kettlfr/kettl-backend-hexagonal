package com.kettl.features.passwordReset.domain.aggregate

import com.kettl.features.user.domain.User

data class ConfirmPasswordResetAggregate(
    val user: User,
    val hashedToken: String,
    val newPassword: String
)