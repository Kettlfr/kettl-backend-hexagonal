package com.kettl.features.passwordReset.infra.adapters

import com.datastax.oss.driver.api.core.cql.BatchStatement
import com.datastax.oss.driver.api.core.cql.DefaultBatchType
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.kettl.config.Config
import com.kettl.database.scylla.ScyllaGateway
import com.kettl.features.passwordReset.domain.PasswordReset
import com.kettl.features.passwordReset.domain.aggregate.ConfirmPasswordResetAggregate
import com.kettl.features.passwordReset.domain.ports.PasswordResetRepository
import com.kettl.features.shared.domain.provider.HasherProvider
import com.kettl.features.user.infra.adapters.UserScyllaRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PasswordResetScyllaRepository : PasswordResetRepository, KoinComponent {

    companion object {
        private const val PASSWORD_RESET_TABLE = "password_reset"
        private const val PASSWORD_RESET_TABLE_BY_TOKEN = "password_reset_by_token"

        val INSERT_PASSWORD_RESET_STATEMENT =
            QueryBuilder.insertInto(Config.SCYLLA_DEFAULT_KEYSPACE, PASSWORD_RESET_TABLE)
                .value("email", QueryBuilder.bindMarker()).value("reset_token", QueryBuilder.bindMarker());

        val SELECT_PASSWORD_RESET_STATEMENT =
            QueryBuilder.selectFrom(Config.SCYLLA_DEFAULT_KEYSPACE, PASSWORD_RESET_TABLE).column("email")
                .column("reset_token").whereColumn("email").isEqualTo(QueryBuilder.bindMarker());

        val DELETE_PASSWORD_RESET =
            QueryBuilder.deleteFrom(Config.SCYLLA_DEFAULT_KEYSPACE, PASSWORD_RESET_TABLE).whereColumn("email")
                .isEqualTo(QueryBuilder.bindMarker());

        val SELECT_PASSWORD_RESET_BY_TOKEN_STATEMENT =
            QueryBuilder.selectFrom(Config.SCYLLA_DEFAULT_KEYSPACE, PASSWORD_RESET_TABLE_BY_TOKEN).column("email")
                .column("reset_token").whereColumn("reset_token").isEqualTo(QueryBuilder.bindMarker());


    }

    private val scyllaGateway: ScyllaGateway by inject()
    private val hasherProvider: HasherProvider by inject(named("argon"))

    override suspend fun create(passwordReset: PasswordReset): PasswordReset {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(INSERT_PASSWORD_RESET_STATEMENT.build());

        session.execute(
            prepared.bind().setString("reset_token", passwordReset.hashedToken).setString("email", passwordReset.email)
        )
        return passwordReset
    }

    override suspend fun find(email: String): PasswordReset? {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(SELECT_PASSWORD_RESET_STATEMENT.build());

        val resultSet = session.execute(
            prepared.bind().setString("email", email)
        )
        val row = resultSet.one()
        if (row != null) {
            return PasswordReset(
                hashedToken = row.getString("reset_token") ?: "",
                email = row.getString("email") ?: "",
            )
        }
        return null
    }

    override suspend fun findByToken(token: String): PasswordReset? {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(SELECT_PASSWORD_RESET_BY_TOKEN_STATEMENT.build());

        val resultSet = session.execute(
            prepared.bind().setString("reset_token", token)
        )
        val row = resultSet.one()
        if (row != null) {
            return PasswordReset(
                hashedToken = row.getString("reset_token") ?: "",
                email = row.getString("email") ?: "",
            )
        }
        return null
    }

    override suspend fun confirmPasswordResetAggregate(confirmPasswordResetAggregate: ConfirmPasswordResetAggregate): Boolean {
        val session = scyllaGateway.getSession()

        val updatedUser = confirmPasswordResetAggregate.user.copy(
            hashedPassword = hasherProvider.hash(confirmPasswordResetAggregate.newPassword)
        )
        val batchStatement = BatchStatement.builder(DefaultBatchType.LOGGED)
            .addStatement(
                session.prepare(DELETE_PASSWORD_RESET.build()).bind()
                    .setString("email", confirmPasswordResetAggregate.user.email))
            .addStatement(
                session.prepare(UserScyllaRepository.UPDATE_USER_STATEMENT.build()).bind()
                    .setLong("id", updatedUser.id.id)
                    .setString("email", updatedUser.email).setBoolean("email_verified", updatedUser.emailVerified)
                    .setString("first_name", updatedUser.firstName).setString("last_name", updatedUser.lastName)
                    .setString("password", updatedUser.hashedPassword)
                    .setString("phone_number", updatedUser.phoneNumber)
            ).build()

        val rs: ResultSet = session.execute(batchStatement)
        return rs.wasApplied() // TODO: check if it's correct because I think it returns true even if the batch is not applied (And revert the changes)
    }

}