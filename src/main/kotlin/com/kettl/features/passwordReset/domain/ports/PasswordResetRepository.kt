package com.kettl.features.passwordReset.domain.ports

import com.kettl.features.passwordReset.domain.PasswordReset
import com.kettl.features.passwordReset.domain.aggregate.ConfirmPasswordResetAggregate

interface PasswordResetRepository {

    suspend fun create(passwordReset: PasswordReset): PasswordReset

    suspend fun find(email: String): PasswordReset?

    suspend fun findByToken(token: String): PasswordReset?

    suspend fun confirmPasswordResetAggregate(confirmPasswordResetAggregate: ConfirmPasswordResetAggregate): Boolean

}