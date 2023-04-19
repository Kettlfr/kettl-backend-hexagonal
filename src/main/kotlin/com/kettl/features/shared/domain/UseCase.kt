package com.kettl.features.shared.domain
import com.kettl.features.shared.domain.Result

abstract class UseCase<Input, Output> {

    abstract suspend fun execute(input: Input): Result<Output>

    interface Input

    interface Output
}