package com.hyoseok.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("urls")
data class UrlEntity(
    @Id
    val id: Long? = null,

    @Column
    val encodedUrl: String,

    @Column
    val longUrl: String,

    @Column
    val createdAt: LocalDateTime,
) {

    fun toDomainEntity() = Url(id = id!!, encodedUrl = encodedUrl, longUrl = longUrl, createdAt = createdAt)
}
