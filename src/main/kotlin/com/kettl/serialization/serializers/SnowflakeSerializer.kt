package com.kettl.serialization.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.kettl.features.shared.domain.Snowflake

class SnowflakeSerializer : JsonSerializer<Snowflake>() {
    override fun serialize(value: Snowflake?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        value?.id?.let { gen?.writeNumber(it) }
    }

}
