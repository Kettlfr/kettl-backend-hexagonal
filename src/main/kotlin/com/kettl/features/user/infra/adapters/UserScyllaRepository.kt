package com.kettl.features.user.infra.adapters

import com.kettl.database.scylla.ScyllaGateway
import com.kettl.features.shared.domain.Snowflake
import com.kettl.features.user.domain.User
import com.kettl.features.user.domain.ports.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

class UserScyllaRepository : UserRepository, KoinComponent {

    private val scyllaGateway: ScyllaGateway by inject()

    override suspend fun create(user: User): User {
        val session = scyllaGateway.getSession()
        val statement = session.prepare(
            """
            INSERT INTO kettl.users (id, created_at, email, email_verified, first_name, last_name, password, phone_number)
            VALUES (:id, :created_at, :email, :email_verified, :first_name, :last_name, :password, :phone_number)
            """
        ).bind()
            .setLong("id", user.id.id)
            .setInstant("created_at", user.createdAt)
            .setString("email", user.email)
            .setBoolean("email_verified", user.emailVerified)
            .setString("first_name", user.firstName)
            .setString("last_name", user.lastName)
            .setString("password", user.hashedPassword)
            .setString("phone_number", user.phoneNumber)

        session.execute(statement)
        return user
    }

    override suspend fun save(user: User): User {
        val session = scyllaGateway.getSession()
        val statement = session.prepare(
            """
            UPDATE kettl.users SET
                email = :email,
                email_verified = :email_verified,
                first_name = :first_name,
                last_name = :last_name,
                password = :password,
                phone_number = :phone_number
            WHERE id = :id
            """
        ).bind()
            .setLong("id", user.id.id)
            .setString("email", user.email)
            .setBoolean("email_verified", user.emailVerified)
            .setString("first_name", user.firstName)
            .setString("last_name", user.lastName)
            .setString("password", user.hashedPassword)
            .setString("phone_number", user.phoneNumber)

        session.execute(statement)
        return user
    }

    override suspend fun find(id: Snowflake): User? {
        val session = scyllaGateway.getSession()
        val statement = session.prepare(
            """
            SELECT * FROM kettl.users WHERE id = :id
            """
        ).bind()
            .setLong("id", id.id)

        val result = session.execute(statement)
        val row = result.one() ?: return null

        return User(
            id = Snowflake(row.getLong("id")),
            email = row.getString("email") ?: "",
            firstName = row.getString("first_name") ?: "",
            lastName = row.getString("last_name") ?: "",
            phoneNumber = row.getString("phone_number") ?: "",
            emailVerified = row.getBoolean("email_verified") ?: false,
            createdAt = row.getInstant("created_at") ?: Instant.now(),
            hashedPassword = row.getString("password") ?: "",
        )
    }

    override suspend fun findByEmail(email: String): User? {
        val session = scyllaGateway.getSession()
        val statement = session.prepare(
            """
            SELECT * FROM kettl.users WHERE email = :email
            """
        ).bind()
            .setString("email", email)

        val result = session.execute(statement)
        val row = result.one() ?: return null

        return User(
            id = Snowflake(row.getLong("id")),
            email = row.getString("email") ?: "",
            firstName = row.getString("first_name") ?: "",
            lastName = row.getString("last_name") ?: "",
            phoneNumber = row.getString("phone_number") ?: "",
            emailVerified = row.getBoolean("email_verified") ?: false,
            createdAt = row.getInstant("created_at") ?: Instant.now(),
            hashedPassword = row.getString("password") ?: "",
        )
    }
}