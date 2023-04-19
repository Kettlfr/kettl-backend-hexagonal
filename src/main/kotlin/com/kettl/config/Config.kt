package com.kettl.config

import java.io.File

object Config {
    val NODE_ID: Long = System.getenv()["NODE_ID"]?.toLong() ?: 0L
    val API_VERSION: String = System.getenv()["API_VERSION"] ?: "alpha-0.1.0"
    val SCYLLA_HOST: String = System.getenv()["SCYLLA_HOST"] ?: "localhost"
    val SCYLLA_PORT: Int = System.getenv()["SCYLLA_PORT"]?.toInt() ?: 9042
    val SCYLLA_USER: String = System.getenv()["SCYLLA_USER"] ?: "cassandra"
    val SCYLLA_PASSWORD: String = System.getenv()["SCYLLA_PASSWORD"] ?: "cassandra"
    val SCYLLA_DEFAULT_KEYSPACE: String = System.getenv()["SCYLLA_DEFAULT_KEYSPACE"] ?: "kettl"
}
