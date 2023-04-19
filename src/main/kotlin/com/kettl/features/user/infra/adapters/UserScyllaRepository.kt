package com.kettl.features.user.infra.adapters

import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.kettl.config.Config
import com.kettl.database.scylla.ScyllaGateway
import com.kettl.features.passwordReset.infra.adapters.PasswordResetScyllaRepository
import com.kettl.features.shared.domain.Snowflake
import com.kettl.features.user.domain.User
import com.kettl.features.user.domain.ports.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

class UserScyllaRepository : UserRepository, KoinComponent {

    companion object {
        private const val USER_TABLE = "users"

        val INSERT_USER_STATEMENT = QueryBuilder.insertInto(Config.SCYLLA_DEFAULT_KEYSPACE, USER_TABLE)
            .value("id", QueryBuilder.bindMarker())
            .value("created_at", QueryBuilder.bindMarker())
            .value("email", QueryBuilder.bindMarker())
            .value("email_verified", QueryBuilder.bindMarker())
            .value("first_name", QueryBuilder.bindMarker())
            .value("last_name", QueryBuilder.bindMarker())
            .value("password", QueryBuilder.bindMarker())
            .value("phone_number", QueryBuilder.bindMarker())

        val UPDATE_USER_STATEMENT = QueryBuilder.update(Config.SCYLLA_DEFAULT_KEYSPACE, USER_TABLE)
            .setColumn("email", QueryBuilder.bindMarker())
            .setColumn("email_verified", QueryBuilder.bindMarker())
            .setColumn("first_name", QueryBuilder.bindMarker())
            .setColumn("last_name", QueryBuilder.bindMarker())
            .setColumn("password", QueryBuilder.bindMarker())
            .setColumn("phone_number", QueryBuilder.bindMarker())
            .whereColumn("id").isEqualTo(QueryBuilder.bindMarker())

        val SELECT_USER_STATEMENT = QueryBuilder.selectFrom(Config.SCYLLA_DEFAULT_KEYSPACE, USER_TABLE)
            .all()
            .whereColumn("id").isEqualTo(QueryBuilder.bindMarker())

        val SELECT_USER_BY_EMAIL_STATEMENT = QueryBuilder.selectFrom(Config.SCYLLA_DEFAULT_KEYSPACE, USER_TABLE)
            .all()
            .whereColumn("email").isEqualTo(QueryBuilder.bindMarker())

        val DELETE_USER_STATEMENT = QueryBuilder.deleteFrom(Config.SCYLLA_DEFAULT_KEYSPACE, USER_TABLE)
            .whereColumn("id").isEqualTo(QueryBuilder.bindMarker())
    }

    private val scyllaGateway: ScyllaGateway by inject()

    override suspend fun create(user: User): User {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(INSERT_USER_STATEMENT.build());

        session.execute(
            prepared.bind()
                .setLong("id", user.id.id)
                .setInstant("created_at", user.createdAt)
                .setString("email", user.email)
                .setBoolean("email_verified", user.emailVerified)
                .setString("first_name", user.firstName)
                .setString("last_name", user.lastName)
                .setString("password", user.hashedPassword)
                .setString("phone_number", user.phoneNumber)
        )
        return user
    }

    override suspend fun save(user: User): User {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(UPDATE_USER_STATEMENT.build());

        session.execute(
            prepared.bind()
                .setString("email", user.email)
                .setBoolean("email_verified", user.emailVerified)
                .setString("first_name", user.firstName)
                .setString("last_name", user.lastName)
                .setString("password", user.hashedPassword)
                .setString("phone_number", user.phoneNumber)
                .setLong("id", user.id.id)
        )
        return user
    }

    override suspend fun find(id: Snowflake): User? {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(SELECT_USER_STATEMENT.build());

        val result = session.execute(
            prepared.bind()
                .setLong("id", id.id)
        )
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
        val prepared = session.prepare(SELECT_USER_BY_EMAIL_STATEMENT.build());

        val result = session.execute(
            prepared.bind()
                .setString("email", email)
        )
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

    override suspend fun delete(id: Snowflake): Boolean {
        val session = scyllaGateway.getSession()
        val prepared = session.prepare(DELETE_USER_STATEMENT.build());

        session.execute(
            prepared.bind()
                .setLong("id", id.id)
        )
        return true
    }
}