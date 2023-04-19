package com.kettl.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import com.kettl.features.shared.domain.Snowflake
import com.kettl.serialization.deserializers.SnowflakeDeserializer
import com.kettl.serialization.serializers.SnowflakeSerializer

class ModelsModule : SimpleModule(ModelsModule::class.java.name) {
    init {
        addSerializer(Snowflake::class.java, SnowflakeSerializer())
        addDeserializer(Snowflake::class.java, SnowflakeDeserializer())
    }
}