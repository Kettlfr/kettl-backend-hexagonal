package com.kettl.features.shared.domain

import kotlinx.serialization.Serializable

@Serializable
class Result<T>(private val value: T?, private val failure: Failure? = null) {
    companion object {
        fun <T : Any> success(value: T): Result<T> {
            return Result(value)
        }

        fun <T : Any> failure(error: String): Result<T> {
            return Result(null, SimpleFailure(error))
        }

        fun <T : Any> failure(exception: Exception): Result<T> {
            return Result(null, SimpleFailure(exception))
        }

        fun <T : Any> failure(error: String, message: String, detail: String): Result<T> {
            return Result(null, DetailedFailure(error, message, detail))
        }
    }

    interface Failure

    class SimpleFailure(val error: String) : Failure {
        constructor(exception: Exception) : this(exception.javaClass.simpleName)
    }

    class DetailedFailure(val error: String, val message: String, val detail: String) : Failure {
        constructor(exception: Exception) : this(exception.javaClass.simpleName, exception.message
            ?: "", exception.stackTraceToString())
    }

    fun get(): T {
        return value ?: throw IllegalStateException("Result is failure")
    }

    fun getOrNull(): T? {
        return value
    }

    fun getFailure(): Failure {
        return failure ?: throw IllegalStateException("Result is success")
    }

    fun getFailureOrNull(): Failure? {
        return failure
    }

    fun isSuccess(): Boolean {
        return value != null
    }

    fun isFailure(): Boolean {
        return value == null
    }
}