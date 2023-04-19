package com.kettl.features.shared.domain.provider

interface CharSequenceProvider {

    fun generate(length: Long): String
}