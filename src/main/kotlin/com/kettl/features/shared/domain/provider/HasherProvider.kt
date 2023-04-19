package com.kettl.features.shared.domain.provider

interface HasherProvider {
    fun hash(input: String): String
}