package com.bestdev.repository.shipping

import com.bestdev.entity.shipping.ShippingJpaEntity
import com.bestdev.shipping.entity.Shipping
import com.bestdev.shipping.repository.ShippingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ShippingJpaRepositoryAdapter(
    private val shippingJpaRepository: ShippingJpaRepository,
) : ShippingRepository {

    override fun save(shipping: Shipping) {
        val shippingJpaEntity = ShippingJpaEntity(shipping = shipping)
        shippingJpaRepository.save(shippingJpaEntity)
        shippingJpaEntity.mapDomainEntityId(shipping = shipping)
    }

    override fun saveAll(shippings: List<Shipping>) {
        shippings.forEach { save(shipping = it) }
    }

    override fun findAllByOrderId(orderId: Long): List<Shipping> =
        shippingJpaRepository.findAllByOrderId(orderId = orderId)
            .map { it.toDomainEntity() }
}
