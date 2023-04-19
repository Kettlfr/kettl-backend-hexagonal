package com.kettl.features.passwordReset.infra.adapters

import com.kettl.database.scylla.ScyllaGateway
import com.kettl.features.passwordReset.domain.PasswordReset
import com.kettl.features.passwordReset.domain.ports.PasswordResetRepository
import com.kettl.features.shared.domain.Snowflake
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PasswordResetScyllaRepository : PasswordResetRepository, KoinComponent {

    private val scyllaGateway: ScyllaGateway by inject()

    override suspend fun create(passwordReset: PasswordReset): PasswordReset {
        val session = scyllaGateway.getSession()
        val statement = session.prepare(
            """
            INSERT INTO kettl.password_reset (reset_token, user_id)
            VALUES (:reset_token, :user_id)
            """
        ).bind()
            .setString("reset_token", passwordReset.hashedToken)
            .setLong("user_id", passwordReset.userId.id)

        session.execute(statement)
        return passwordReset
    }

    override suspend fun find(token: String): PasswordReset? {
        val session = scyllaGateway.getSession()
        val statement = session.prepare(
            """
            SELECT * FROM kettl.password_reset WHERE reset_token = :reset_token
            """
        ).bind()
            .setString("reset_token", token)

        val result = session.execute(statement).one()
        return if (result != null) {
            PasswordReset(
                hashedToken = result.getString("reset_token") ?: "",
                userId = Snowflake(result.getLong("user_id"))
            )
        } else {
            null
        }
    }
}