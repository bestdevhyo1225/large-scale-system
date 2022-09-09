package com.hyoseok.entity.sns

import com.hyoseok.sns.entity.SnsImage
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "sns_image")
@DynamicUpdate
class SnsImageJpaEntity private constructor(
    url: String,
    sortOrder: Int,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "url", nullable = false)
    var url: String = url
        protected set

    @Column(name = "sort_order", nullable = false)
    var sortOrder: Int = sortOrder
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_id", nullable = false)
    var snsJpaEntity: SnsJpaEntity? = null
        protected set

    fun changeSnsJpaEntity(snsJpaEntity: SnsJpaEntity) {
        this.snsJpaEntity = snsJpaEntity
    }

    fun toDomainEntity() = SnsImage(id = id!!, url = url, sortOrder = sortOrder)

    companion object {
        operator fun invoke(snsImage: SnsImage): SnsImageJpaEntity =
            SnsImageJpaEntity(url = snsImage.url, sortOrder = snsImage.sortOrder)
    }
}
