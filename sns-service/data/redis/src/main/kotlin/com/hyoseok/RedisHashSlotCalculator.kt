package com.hyoseok

import com.hyoseok.exception.Message.CANNOT_USED_HASH_TAG
import io.lettuce.core.codec.CRC16

object RedisHashSlotCalculator {
    private const val HASH_SLOT_COUNT = 16384

    fun getHashSlot(key: String): Int {
        val (open: Char, close: Char) = listOf('{', '}')
        val openIndex: Int = key.indexOf(open)
        val closeIndex: Int = key.indexOf(close)

        if (openIndex != -1 && closeIndex != -1 && openIndex < closeIndex) {
            throw IllegalArgumentException(CANNOT_USED_HASH_TAG)
        }

        return CRC16.crc16(key.toByteArray()).mod(HASH_SLOT_COUNT)
    }
}
