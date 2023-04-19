package com.kettl.util

import com.kettl.features.shared.domain.Result

class ResultUtil {

    companion object {
        fun <T : Any> unwrapFailure(result: Result<T>): Map<String, String> {
            if (result.isSuccess()) {
                throw IllegalArgumentException("Result is success")
            }
            return when (result.getFailure()) {
                is Result.DetailedFailure -> {
                    val failure = result.getFailure() as Result.DetailedFailure
                    return mapOf(
                        "error" to failure.error, "message" to failure.message, "detail" to failure.detail
                    )
                }

                is Result.SimpleFailure -> {
                    val failure = result.getFailure() as Result.SimpleFailure
                    return mapOf(
                        "error" to failure.error
                    )
                }
                else -> {
                    throw IllegalArgumentException("Unknown failure type")
                }
            }
        }
    }
}