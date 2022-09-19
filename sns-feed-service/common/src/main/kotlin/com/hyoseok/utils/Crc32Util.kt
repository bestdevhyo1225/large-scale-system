package com.hyoseok.utils

import java.util.zip.CRC32

object Crc32Util {
    private val crc32 = CRC32()

    fun encode(value: String): String {
        return runCatching {
            crc32.update(value.toByteArray())
            crc32.value.toString(radix = 16)
        }.getOrElse {
            throw it
        }
    }
}
