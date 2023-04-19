package com.kettl.serialization.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.kettl.features.shared.domain.Snowflake

class SnowflakeDeserializer : JsonDeserializer<Snowflake>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Snowflake {
        return Snowflake(p?.valueAsLong ?: 0)
    }

}
