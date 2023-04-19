package com.kettl.database.scylla.codecs

import com.datastax.oss.driver.api.core.ProtocolVersion
import com.datastax.oss.driver.api.core.type.DataType
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.core.type.codec.TypeCodec
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs
import com.datastax.oss.driver.api.core.type.reflect.GenericType
import com.kettl.features.shared.domain.Snowflake
import java.nio.ByteBuffer


class SnowflakeTypeCodec : TypeCodec<Snowflake> {

    override fun getJavaType(): GenericType<Snowflake> {
        return GenericType.of(Snowflake::class.java)
    }

    override fun getCqlType(): DataType {
        return DataTypes.BIGINT;
    }

    override fun decode(
        bytes: ByteBuffer?,
        protocolVersion: ProtocolVersion,
    ): Snowflake? {
        return TypeCodecs.BIGINT.decode(bytes, protocolVersion)?.let { Snowflake(it) }
    }

    override fun parse(
        value: String?,
    ): Snowflake? {
        return TypeCodecs.BIGINT.parse(value)?.let {
            Snowflake(it)
        }
    }

    override fun format(
        value: Snowflake?,
    ): String {
        return TypeCodecs.BIGINT.format(value?.id)
    }

    override fun encode(
        value: Snowflake?,
        protocolVersion: ProtocolVersion,
    ): ByteBuffer? {
        return TypeCodecs.BIGINT.encode(value?.id, protocolVersion)
    }
}
