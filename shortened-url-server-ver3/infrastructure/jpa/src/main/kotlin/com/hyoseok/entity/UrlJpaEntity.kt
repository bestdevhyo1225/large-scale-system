package com.hyoseok.entity

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "urls",
    indexes = [
        Index(name = "uix_urls_encodedUrl", columnList = "encoded_url", unique = true),
        Index(name = "ix_urls_long_url", columnList = "long_url"),
    ],
)
@DynamicUpdate
class UrlJpaEntity private constructor(
    encodedUrl: String,
    longUrl: String,
    createdAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column(name = "encoded_url", nullable = false)
    var encodedUrl: String = encodedUrl
        protected set

    @Column(name = "long_url", nullable = false)
    var longUrl: String = longUrl
        protected set

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "UrlJpaEntity(id=$id, encodedUrl=$encodedUrl, longUrl=$longUrl, createdAt=$createdAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherUrlJpaEntity = (other as? UrlJpaEntity) ?: return false
        return this.id == otherUrlJpaEntity.id
    }

    fun mapDomainEntity(url: Url) {
        url.changeId(id = id)
    }

    fun toDomainEntity() = Url(id = id, encodedUrl = encodedUrl, longUrl = longUrl, createdAt = createdAt)

    companion object {
        operator fun invoke(url: Url) = with(receiver = url) {
            UrlJpaEntity(
                encodedUrl = encodedUrl,
                longUrl = longUrl,
                createdAt = createdAt,
            )
        }
    }
}
