package com.hyoseok.repository.product

import com.hyoseok.entity.product.ExternalProductJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExternalProductJpaRepository : JpaRepository<ExternalProductJpaEntity, Long>
