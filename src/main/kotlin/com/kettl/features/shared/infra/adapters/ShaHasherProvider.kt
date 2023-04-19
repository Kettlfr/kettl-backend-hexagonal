package com.kettl.features.shared.infra.adapters

import com.kettl.features.shared.domain.provider.HasherProvider
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ShaHasherProvider : HasherProvider {
    override fun hash(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-512")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}
