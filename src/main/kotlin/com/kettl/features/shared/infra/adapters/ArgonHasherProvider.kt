package com.kettl.features.shared.infra.adapters

import com.kettl.features.shared.domain.provider.HasherProvider
import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

class ArgonHasherProvider : HasherProvider {

    private val argon: Argon2 = Argon2Factory.create()

    override fun hash(input: String): String {
        return this.argon.hash(20, 65536, 22, input.toCharArray());
    }

}