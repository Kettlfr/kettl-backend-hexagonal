package com.kettl.features.passwordReset.domain

import com.kettl.features.passwordReset.domain.aggregate.ConfirmPasswordResetAggregate
import com.kettl.features.passwordReset.domain.ports.PasswordResetRepository
import com.kettl.features.shared.domain.Result
import com.kettl.features.shared.domain.UseCase
import com.kettl.features.shared.domain.provider.HasherProvider
import com.kettl.features.user.domain.ports.UserRepository

class ConfirmPasswordResetUseCase(
    private val passwordResetRepository: PasswordResetRepository,
    private val hasherProvider: HasherProvider,
    private val userRepository: UserRepository
) : UseCase<ConfirmPasswordResetUseCase.Input, String>() {

    class Input(
        val token: String, val newPassword: String
    ) : UseCase.Input

    override suspend fun execute(input: Input): Result<String> {
        val hashedToken = this.hasherProvider.hash(input.token)

        val passwordReset = passwordResetRepository.findByToken(hashedToken) ?: return Result.failure(
            "passwordReset-confirmPasswordReset-0001", "Invalid token", "Ensure that the token is valid"
        )

        val user = userRepository.findByEmail(passwordReset.email) ?: return Result.failure(
            "passwordReset-confirmPasswordReset-0002", "Account not found", "Account may have been deleted or disabled"
        )

        if (passwordResetRepository.confirmPasswordResetAggregate(
                ConfirmPasswordResetAggregate(
                    user = user, hashedToken = hashedToken, newPassword = input.newPassword
                )
            ).not()
        ) {
            return Result.failure("passwordReset-confirmPasswordReset-0003", "Internal error", "Please try again later")
        }
        return Result.success("OK")
    }
}