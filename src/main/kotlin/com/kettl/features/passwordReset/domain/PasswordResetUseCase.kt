package com.kettl.features.passwordReset.domain

import com.kettl.ext.isEmailValid
import com.kettl.features.passwordReset.domain.ports.PasswordResetRepository
import com.kettl.features.shared.domain.Result
import com.kettl.features.shared.domain.UseCase
import com.kettl.features.shared.domain.provider.CharSequenceProvider
import com.kettl.features.shared.domain.provider.HasherProvider
import com.kettl.features.user.domain.User
import com.kettl.features.user.domain.ports.UserRepository


// TODO: Max 3 tokens per user.
class PasswordResetUseCase(
    private val userRepository: UserRepository,
    private val passwordResetRepository: PasswordResetRepository,
    private val hasherProvider: HasherProvider,
    private val charSequenceProvider: CharSequenceProvider,
) : UseCase<PasswordResetUseCase.Input, String>() {

    class Input(
        val email: String,
    ) : UseCase.Input

    override suspend fun execute(input: Input): Result<String> {
        when {
            input.email.isEmpty() -> return Result.failure("passwordReset-passwordReset-0001", "Email is required", "Ensure that the email is not empty")
            input.email.isEmailValid().not() -> return Result.failure("passwordReset-passwordReset-0002", "Email is invalid", "Ensure that the email is valid")
        }
        val user: User? = this.userRepository.findByEmail(input.email)
        if (user != null) {
            val realToken = this.charSequenceProvider.generate(32)
            val hashedToken = this.hasherProvider.hash(realToken)

            this.passwordResetRepository.create(
                PasswordReset(
                    hashedToken = hashedToken,
                    email = input.email,
                )
            )

            // TODO: Send email with link to reset password.
            println("realToken: $realToken") // Please don't punish me for this :) It's just for the demo.
        }
        return Result.success("Password reset link has been sent to your email")
    }
}