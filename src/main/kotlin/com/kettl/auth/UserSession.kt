package com.kettl.auth

import io.ktor.server.auth.*

data class UserSession(val id: Long, val email: String, val emailVerified: Boolean) : Principal