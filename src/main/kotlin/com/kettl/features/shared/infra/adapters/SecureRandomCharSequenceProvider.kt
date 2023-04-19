package com.kettl.features.shared.infra.adapters

import com.kettl.features.shared.domain.provider.CharSequenceProvider
import java.security.SecureRandom
import kotlin.streams.asSequence

class SecureRandomCharSequenceProvider : CharSequenceProvider {

    companion object {
        val CHAR_POOL : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    }

    private val secureRandom = SecureRandom();

    override fun generate(length: Long): String {
        return secureRandom.ints(length, 0, CHAR_POOL.size)
            .asSequence()
            .map(CHAR_POOL::get)
            .joinToString("")
    }
}