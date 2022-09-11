package com.hyoseok.sns.entity

import com.hyoseok.product.entity.ExternalProduct
import java.time.LocalDateTime

data class SnsCache(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val isDisplay: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val snsImages: List<SnsImage>,
    val snsTag: SnsTag,
    val products: List<ExternalProduct>,
)
