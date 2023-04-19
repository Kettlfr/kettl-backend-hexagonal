package com.kettl.ext

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import com.kettl.features.shared.domain.Result
import com.kettl.util.ResultUtil

suspend inline fun <reified T : Any> ApplicationCall.respondResult(result: Result<T>) {
    if (result.isSuccess()) {
        respond(result.get())
    } else {
        respond(ResultUtil.unwrapFailure(result))
    }
}