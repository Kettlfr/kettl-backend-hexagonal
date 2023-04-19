package com.kettl.database.scylla

import com.datastax.oss.driver.api.core.CqlSession
import com.kettl.config.Config
import com.kettl.database.scylla.codecs.SnowflakeTypeCodec

class ScyllaGateway {

    private val cqlSession: CqlSession = CqlSession.builder()
        //.addContactPoint(InetSocketAddress(Config.SCYLLA_HOST, Config.SCYLLA_PORT))
        .withAuthCredentials(Config.SCYLLA_USER, Config.SCYLLA_PASSWORD).addTypeCodecs(
            SnowflakeTypeCodec()
        ).build()

    fun getSession(): CqlSession {
        return this.cqlSession
    }
}