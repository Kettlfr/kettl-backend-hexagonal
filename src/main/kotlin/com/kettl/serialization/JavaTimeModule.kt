package com.kettl.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import com.kettl.serialization.deserializers.JavaTimeInstantDeserializer
import com.kettl.serialization.serializers.JavaTimeInstantSerializer

class JavaTimeModule : SimpleModule(JavaTimeModule::class.java.name) {
    init {
        addSerializer(java.time.Instant::class.java, JavaTimeInstantSerializer())
        addDeserializer(java.time.Instant::class.java, JavaTimeInstantDeserializer())
    }
}