package com.kettl.features.shared.domain

data class Snowflake(
    val id: Long = 0) {

    companion object {
        private const val UNUSED_BITS = 1 // Sign bit, Unused (always set to 0)
        private const val EPOCH_BITS = 41
        private const val NODE_ID_BITS = 10
        private const val SEQUENCE_BITS = 12

        private const val maxNodeId = (1L shl NODE_ID_BITS) - 1
        private const val maxSequence = (1L shl SEQUENCE_BITS) - 1

        // Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
        private const val DEFAULT_CUSTOM_EPOCH = 1420070400000L
    }

    fun isValid(): Boolean {
        val maskNodeId = (1L shl NODE_ID_BITS) - 1 shl SEQUENCE_BITS
        val maskSequence = (1L shl SEQUENCE_BITS) - 1
        val timestamp = (id shr NODE_ID_BITS + SEQUENCE_BITS)
        val nodeId = id and maskNodeId shr SEQUENCE_BITS
        val sequence = id and maskSequence
        return timestamp >= 0 && nodeId >= 0 && sequence >= 0
    }
}