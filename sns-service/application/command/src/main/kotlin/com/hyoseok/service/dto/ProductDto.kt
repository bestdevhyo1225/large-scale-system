package com.hyoseok.service.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.hyoseok.product.entity.ExternalProduct
import java.time.LocalDateTime

data class ProductCreateDto(
    val productId: Long,
    val imageUrl: String,
    val name: String,
    val price: Int,
    val isSale: Boolean,
    val isSoldout: Boolean,
) {
    companion object {
        fun toEntities(products: List<ProductCreateDto>, snsId: Long, memberId: Long) =
            products.map {
                with(receiver = it) {
                    ExternalProduct(
                        productId = productId,
                        imageUrl = imageUrl,
                        name = name,
                        price = price,
                        isSale = isSale,
                        isSoldout = isSoldout,
                        snsId = snsId,
                        memberId = memberId,
                    )
                }
            }
    }
}

data class ProductEditDto(
    val id: Long,
    val productId: Long,
    val imageUrl: String,
    val name: String,
    val price: Int,
    val isSale: Boolean,
    val isSoldout: Boolean,
)

data class ProductFindResultDto(
    val id: Long,
    val productId: Long,
    val imageUrl: String,
    val name: String,
    val price: Int,
    val isSale: Boolean,
    val isSoldout: Boolean,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(externalProduct: ExternalProduct) =
            with(receiver = externalProduct) {
                ProductFindResultDto(
                    id = id!!,
                    productId = productId,
                    imageUrl = imageUrl,
                    name = name,
                    price = price,
                    isSale = isSale,
                    isSoldout = isSoldout,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}
