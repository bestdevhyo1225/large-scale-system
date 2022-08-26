package com.hyoseok.entity

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "urls")
@DynamicUpdate
class UrlJpaEntity private constructor(
    id: Long,
    shortUrl: String,
    longUrl: String,
    createdAt: LocalDateTime,
) {

    @Id
    var id: Long = id
        protected set

    @Column(name = "short_url", nullable = false)
    var shortUrl: String = shortUrl
        protected set

    @Column(name = "long_url", nullable = false)
    var longUrl: String = longUrl
        protected set

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "UrlJpaEntity(id=$id, shortUrl=$shortUrl, longUrl=$longUrl, createdAt=$createdAt)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherUrlJpaEntity = (other as? UrlJpaEntity) ?: return false
        return this.id == otherUrlJpaEntity.id
    }

    fun toDomainEntity() = Url(id = id, shortUrl = shortUrl, longUrl = longUrl, createdAt = createdAt)

    companion object {
        operator fun invoke(url: Url) = with(receiver = url) {
            println("current: $this")
            UrlJpaEntity(
                id = id,
                shortUrl = shortUrl,
                longUrl = longUrl,
                createdAt = createdAt,
            )
        }
    }
}
