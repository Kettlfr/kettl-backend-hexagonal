package com.kettl.features.user.domain

import com.kettl.features.shared.domain.UseCase
import com.kettl.features.shared.domain.Result
import com.kettl.features.shared.domain.Snowflake
import com.kettl.features.shared.domain.provider.HasherProvider
import com.kettl.features.shared.domain.provider.IDProvider
import com.kettl.features.user.domain.ports.UserRepository
import kotlinx.coroutines.awaitAll

class UserSignupUseCase(
    private val userRepository: UserRepository,
    private val hasherProvider: HasherProvider,
    private val idProvider: IDProvider,
) :
    UseCase<UserSignupUseCase.Input, User>() {

    class Input(
        val unregisterUser: UnregisteredUser,
    ) : UseCase.Input

    override suspend fun execute(input: Input): Result<User> {
        val unregisteredUser = input.unregisterUser
        when {
            unregisteredUser.email.isEmpty() -> return Result.failure("auth-register-0001", "Email is required", "Ensure that the email is not empty")
            unregisteredUser.clearPassword.isEmpty() -> return Result.failure("auth-register-0002", "Password is required", "Ensure that the password is not empty")
            unregisteredUser.firstName.isEmpty() -> return Result.failure("auth-register-0003", "First name is required", "Ensure that the first name is not empty")
            unregisteredUser.lastName.isEmpty() -> return Result.failure("auth-register-0004", "Last name is required", "Ensure that the last name is not empty")
            this.userRepository.findByEmail(unregisteredUser.email) != null -> return Result.failure("auth-register-0005", "Email already exists", "Ensure that the email is not already used")
        }

        val newUser = User(
            id = idProvider.generate(),
            email = input.unregisterUser.email,
            firstName = input.unregisterUser.firstName,
            lastName = input.unregisterUser.lastName,
            hashedPassword = this.hasherProvider.hash(input.unregisterUser.clearPassword),
        )
        userRepository.create(newUser)
        return Result.success(newUser)
    }
}