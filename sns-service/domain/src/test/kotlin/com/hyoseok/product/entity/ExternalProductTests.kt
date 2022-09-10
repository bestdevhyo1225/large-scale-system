package com.hyoseok.product.entity

import com.hyoseok.utils.Sha256Util
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

internal class ExternalProductTests : DescribeSpec(
    {
        describe("invoke 메서드는") {
            it("ExternalProduct 엔티티를 생성한다.") {
                // given
                val productId = 123124L
                val imageUrl = "https://test.com/19djf"
                val name = "Kotlin 1.0"
                val price = 15_000
                val isSale = true
                val isSoldout = false

                // when
                val externalProduct = ExternalProduct(
                    productId = productId,
                    imageUrl = imageUrl,
                    name = name,
                    price = price,
                    isSale = isSale,
                    isSoldout = isSoldout,
                )

                // then
                externalProduct.productId.shouldBe(productId)
                externalProduct.imageUrl.shouldBe(Sha256Util.encode(value = imageUrl))
                externalProduct.name.shouldBe(name)
                externalProduct.price.shouldBe(price)
                externalProduct.isSale.shouldBe(isSale)
                externalProduct.isSoldout.shouldBe(isSoldout)
                externalProduct.isDisplay.shouldBeTrue()
                externalProduct.createdAt.shouldNotBeNull()
                externalProduct.updatedAt.shouldNotBeNull()
                externalProduct.deletedAt.shouldBeNull()
            }
        }
    },
)
