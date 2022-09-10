package com.hyoseok.web.request

import com.hyoseok.service.dto.ProductCreateDto
import com.hyoseok.service.dto.ProductEditDto
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class ProductCreateRequest(

    @field:Positive(message = "productId는 0보다 큰 값을 입력하세요")
    val productId: Long,

    @field:NotBlank(message = "imageUrl을 입력하세요")
    val imageUrl: String,

    @field:NotBlank(message = "name을 입력하세요")
    val name: String,

    @field:Positive(message = "price는 0보다 큰 값을 입력하세요")
    val price: Int,

    @field:NotNull(message = "isSale을 입력하세요")
    val isSale: Boolean,

    @field:NotNull(message = "isSoldout을 입력하세요")
    val isSoldout: Boolean,
) {
    fun toServiceDto() =
        ProductCreateDto(
            productId = productId,
            imageUrl = imageUrl,
            name = name,
            price = price,
            isSale = isSale,
            isSoldout = isSoldout,
        )
}

data class ProductEditRequest(

    @field:Positive(message = "id는 0보다 큰 값을 입력하세요")
    val id: Long,

    @field:Positive(message = "productId는 0보다 큰 값을 입력하세요")
    val productId: Long,

    @field:NotBlank(message = "imageUrl을 입력하세요")
    val imageUrl: String,

    @field:NotBlank(message = "name을 입력하세요")
    val name: String,

    @field:Positive(message = "price는 0보다 큰 값을 입력하세요")
    val price: Int,

    @field:NotNull(message = "isSale을 입력하세요")
    val isSale: Boolean,

    @field:NotNull(message = "isSoldout을 입력하세요")
    val isSoldout: Boolean,
) {
    fun toServiceDto() =
        ProductEditDto(
            id = id,
            productId = productId,
            imageUrl = imageUrl,
            name = name,
            price = price,
            isSale = isSale,
            isSoldout = isSoldout,
        )
}

