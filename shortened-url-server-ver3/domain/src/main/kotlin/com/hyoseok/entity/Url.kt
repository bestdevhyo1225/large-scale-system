package com.hyoseok.entity

import java.time.LocalDateTime
import java.util.Objects

class Url private constructor(
    id: Long = 0,
    encodedUrl: String,
    longUrl: String,
    createdAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var encodedUrl: String = encodedUrl
        private set

    var longUrl: String = longUrl
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Url(id=$id, encodedUrl=$encodedUrl, longUrl=$longUrl, createdAt=$createdAt)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherUrl = (other as? Url) ?: return false
        return this.id == otherUrl.id &&
            this.encodedUrl == otherUrl.encodedUrl &&
            this.longUrl == otherUrl.longUrl &&
            this.createdAt == otherUrl.createdAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(encodedUrl: String, longUrl: String) =
            Url(encodedUrl = encodedUrl, longUrl = longUrl, createdAt = LocalDateTime.now().withNano(0))

        operator fun invoke(id: Long, encodedUrl: String, longUrl: String, createdAt: LocalDateTime) =
            Url(id = id, encodedUrl = encodedUrl, longUrl = longUrl, createdAt = createdAt)
    }
}
