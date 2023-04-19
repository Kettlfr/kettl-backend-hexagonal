package com.kettl.features.user.domain.ports

import com.kettl.features.shared.domain.Snowflake
import com.kettl.features.user.domain.User

interface UserRepository {

    suspend fun create(user: User): User

    suspend fun save(user: User): User

    suspend fun find(id: Snowflake): User?

    suspend fun findByEmail(email: String): User?

    suspend fun delete(id: Snowflake): Boolean
}