package com.kettl.features.passwordReset.domain.ports

import com.kettl.features.passwordReset.domain.PasswordReset

interface PasswordResetRepository {

    suspend fun create(passwordReset: PasswordReset): PasswordReset

    suspend fun find(token: String): PasswordReset?

}