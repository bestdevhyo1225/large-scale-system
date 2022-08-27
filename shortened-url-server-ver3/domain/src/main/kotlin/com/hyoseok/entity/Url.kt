package com.hyoseok.entity

import java.time.LocalDateTime
import java.util.Objects

class Url private constructor(
    id: Long = 0,
    shortUrl: String,
    longUrl: String,
    createdAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var shortUrl: String = shortUrl
        private set

    var longUrl: String = longUrl
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Url(id=$id, shortUrl=$shortUrl, longUrl=$longUrl, createdAt=$createdAt)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherUrl = (other as? Url) ?: return false
        return this.id == otherUrl.id &&
            this.shortUrl == otherUrl.shortUrl &&
            this.longUrl == otherUrl.longUrl &&
            this.createdAt == otherUrl.createdAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(shortUrl: String, longUrl: String) =
            Url(shortUrl = shortUrl, longUrl = longUrl, createdAt = LocalDateTime.now().withNano(0))

        operator fun invoke(id: Long, shortUrl: String, longUrl: String, createdAt: LocalDateTime) =
            Url(id = id, shortUrl = shortUrl, longUrl = longUrl, createdAt = createdAt)
    }
}
