package com.hyoseok.domain.entity

import com.hyoseok.common.utils.Base62Util
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "urls")
class Url private constructor(
    id: Long,
    shortUrl: String,
    longUrl: String,
    createdAt: LocalDateTime,
) {

    @Id
    var id: Long = id
        protected set

    @Column(nullable = false)
    var shortUrl: String = shortUrl
        protected set

    @Column(nullable = false)
    var longUrl: String = longUrl
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Url(id=$id, shortUrl=$shortUrl, longUrl=$longUrl, createdAt=$createdAt)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherUrl = (other as? Url) ?: return false
        return this.id == otherUrl.id
    }

    companion object {
        operator fun invoke(longUrl: String): Url {
            val id: Long = System.currentTimeMillis()
            return Url(
                id = id,
                shortUrl = Base62Util.encode(value = id),
                longUrl = longUrl,
                createdAt = LocalDateTime.now(),
            )
        }
    }
}
