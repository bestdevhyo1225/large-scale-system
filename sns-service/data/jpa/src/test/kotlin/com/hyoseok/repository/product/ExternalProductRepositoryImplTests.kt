package com.hyoseok.repository.product

import com.hyoseok.product.entity.ExternalProduct
import com.hyoseok.product.repository.ExternalProductRepository
import com.hyoseok.product.repository.read.ExternalProductReadRepository
import com.hyoseok.repository.RepositoryImplTests
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class ExternalProductRepositoryImplTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var externalProductRepository: ExternalProductRepository

    @Autowired
    private lateinit var externalProductReadRepository: ExternalProductReadRepository

    init {
        this.describe("save 메서드는") {
            it("ExternalProduct 엔티티를 저장한다") {
                // given
                val productId = 123124L
                val imageUrl = "https://test.com/19djf"
                val name = "Kotlin 1.0"
                val price = 15_000
                val isSale = true
                val isSoldout = false
                val externalProduct = ExternalProduct(
                    productId = productId,
                    imageUrl = imageUrl,
                    name = name,
                    price = price,
                    isSale = isSale,
                    isSoldout = isSoldout,
                )

                // when
                externalProductRepository.save(externalProduct = externalProduct)

                // then
                val findExternalProduct = externalProductReadRepository.findById(id = externalProduct.id!!)

                findExternalProduct.shouldBe(externalProduct)
            }
        }

        this.describe("saveAll 메서드는") {
            it("ExternalProduct 엔티티 리스트를 저장한다") {
                // given
                val imageUrl = "https://test.com/19djf"
                val name = "Kotlin 1.0"
                val price = 15_000
                val isSale = true
                val isSoldout = false
                val externalProducts = (1L..3L).map {
                    ExternalProduct(
                        productId = it,
                        imageUrl = imageUrl,
                        name = name,
                        price = price,
                        isSale = isSale,
                        isSoldout = isSoldout,
                    )
                }

                // when
                externalProductRepository.saveAll(externalProducts = externalProducts)

                // then
                val findExternalProducts = externalProductReadRepository
                    .findAllByProductIds(productIds = externalProducts.map { it.productId })
                    .sortedBy { it.id }

                findExternalProducts.shouldNotBeEmpty()
                findExternalProducts.forEachIndexed { index, externalProduct ->
                    externalProduct.shouldBe(externalProducts[index])
                }
            }
        }

        this.describe("findByProductId 메서드는") {
            it("ExternalProduct 엔티티를 조회한다") {
                // given
                val productId = 123124L
                val imageUrl = "https://test.com/19djf"
                val name = "Kotlin 1.0"
                val price = 15_000
                val isSale = true
                val isSoldout = false
                val externalProduct = ExternalProduct(
                    productId = productId,
                    imageUrl = imageUrl,
                    name = name,
                    price = price,
                    isSale = isSale,
                    isSoldout = isSoldout,
                )

                externalProductRepository.save(externalProduct = externalProduct)

                // when
                val findExternalProduct =
                    externalProductReadRepository.findByProductId(productId = externalProduct.productId)

                // then
                findExternalProduct.shouldBe(externalProduct)
            }
        }
    }
}
