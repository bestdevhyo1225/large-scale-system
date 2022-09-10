package com.hyoseok.product.entity

import com.hyoseok.utils.Sha256Util
import java.time.LocalDateTime
import java.util.Objects

class ExternalProduct private constructor(
    id: Long? = null,
    productId: Long,
    imageUrl: String,
    name: String,
    price: Int,
    isSale: Boolean,
    isSoldout: Boolean,
    isDisplay: Boolean,
    snsId: Long,
    memberId: Long,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    var id: Long? = id
        private set

    var productId: Long = productId
        private set

    var imageUrl: String = imageUrl
        private set

    var name: String = name
        private set

    var price: Int = price
        private set

    var isSale: Boolean = isSale
        private set

    var isSoldout: Boolean = isSoldout
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var snsId: Long = snsId
        private set

    var memberId: Long = memberId
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "ExternalProduct(id=$id, productId=$productId, imageUrl=$imageUrl, name=$name, price=$price, isSale=$isSale, " +
            "isSoldout=$isSoldout, isDisplay=$isDisplay, snsId=$snsId, memberId=$memberId" +
            "createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherExternalProduct = (other as? ExternalProduct) ?: return false
        return this.id == otherExternalProduct.id &&
            this.productId == otherExternalProduct.productId &&
            this.imageUrl == otherExternalProduct.imageUrl &&
            this.name == otherExternalProduct.name &&
            this.price == otherExternalProduct.price &&
            this.isSale == otherExternalProduct.isSale &&
            this.isSoldout == otherExternalProduct.isSoldout &&
            this.isDisplay == otherExternalProduct.isDisplay &&
            this.snsId == otherExternalProduct.snsId &&
            this.memberId == otherExternalProduct.memberId &&
            this.createdAt == otherExternalProduct.createdAt &&
            this.updatedAt == otherExternalProduct.updatedAt &&
            this.deletedAt == otherExternalProduct.deletedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(
            productId: Long,
            imageUrl: String,
            name: String,
            price: Int,
            isSale: Boolean,
            isSoldout: Boolean,
            snsId: Long,
            memberId: Long,
        ) = ExternalProduct(
            productId = productId,
            imageUrl = Sha256Util.encode(value = imageUrl),
            name = name,
            price = price,
            isSale = isSale,
            isSoldout = isSoldout,
            isDisplay = true,
            snsId = snsId,
            memberId = memberId,
            createdAt = LocalDateTime.now().withNano(0),
            updatedAt = LocalDateTime.now().withNano(0),
        )

        operator fun invoke(
            id: Long,
            productId: Long,
            imageUrl: String,
            name: String,
            price: Int,
            isSale: Boolean,
            isSoldout: Boolean,
            isDisplay: Boolean,
            snsId: Long,
            memberId: Long,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?,
        ) = ExternalProduct(
            id = id,
            productId = productId,
            imageUrl = imageUrl,
            name = name,
            price = price,
            isSale = isSale,
            isSoldout = isSoldout,
            isDisplay = isDisplay,
            snsId = snsId,
            memberId = memberId,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        )
    }
}
