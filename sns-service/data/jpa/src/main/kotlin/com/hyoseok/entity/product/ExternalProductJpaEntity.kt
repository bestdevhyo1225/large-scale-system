package com.hyoseok.entity.product

import com.hyoseok.product.entity.ExternalProduct
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "external_product",
    indexes = [
        Index(name = "uix_external_product_sns_id_product_id", columnList = "sns_id, product_id", unique = true),
    ],
)
class ExternalProductJpaEntity private constructor(
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "product_id", nullable = false)
    var productId: Long = productId
        protected set

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = imageUrl
        protected set

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "price", nullable = false)
    var price: Int = price
        protected set

    @Column(name = "is_sale", nullable = false)
    var isSale: Boolean = isSale
        protected set

    @Column(name = "is_soldout", nullable = false)
    var isSoldout: Boolean = isSoldout
        protected set

    @Column(name = "is_display", nullable = false)
    var isDisplay: Boolean = isDisplay
        protected set

    @Column(name = "sns_id", nullable = false)
    var snsId: Long = snsId
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    fun mapDomainEntity(externalProduct: ExternalProduct) {
        externalProduct.changeId(id = id!!)
    }

    fun toDomainEntity() =
        ExternalProduct(
            id = id!!,
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

    companion object {
        operator fun invoke(externalProduct: ExternalProduct) =
            with(receiver = externalProduct) {
                ExternalProductJpaEntity(
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
}
