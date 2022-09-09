package com.hyoseok.utils

import java.security.DigestException
import java.security.MessageDigest

object Sha256Util {
    fun encode(value: String): String =
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(value.toByteArray())
            bytesToHex(byteArray = messageDigest.digest())
        } catch (exception: CloneNotSupportedException) {
            throw DigestException(exception.localizedMessage)
        }

    private fun bytesToHex(byteArray: ByteArray): String {
        val digits = "0123456789ABCDEF"
        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = digits[v shr 4]
            hexChars[i * 2 + 1] = digits[v and 0xf]
        }
        return String(hexChars)
    }
}
