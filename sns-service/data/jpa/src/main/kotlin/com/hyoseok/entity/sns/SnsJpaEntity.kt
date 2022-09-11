package com.hyoseok.entity.sns

import com.hyoseok.sns.entity.Sns
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "sns")
@DynamicUpdate
class SnsJpaEntity private constructor(
    memberId: Long,
    title: String,
    contents: String,
    writer: String,
    isDisplay: Boolean = true,
    productIds: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "contents", nullable = false)
    var contents: String = contents
        protected set

    @Column(name = "writer", nullable = false)
    var writer: String = writer
        protected set

    @Column(name = "is_display", nullable = false)
    var isDisplay: Boolean = isDisplay
        protected set

    @Column(name = "product_ids", nullable = false)
    var productIds: String = productIds
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

    @OneToMany(mappedBy = "snsJpaEntity", cascade = [CascadeType.PERSIST])
    var snsImageJpaEntities: MutableList<SnsImageJpaEntity> = mutableListOf()

    @OneToMany(mappedBy = "snsJpaEntity", cascade = [CascadeType.PERSIST])
    var snsTagJpaEntities: MutableList<SnsTagJpaEntity> = mutableListOf()

    fun addSnsImageJpaEntity(snsImageJpaEntity: SnsImageJpaEntity) {
        snsImageJpaEntities.add(snsImageJpaEntity)
        snsImageJpaEntity.changeSnsJpaEntity(snsJpaEntity = this)
    }

    fun addSnsTagJpaEntity(snsTagJpaEntity: SnsTagJpaEntity) {
        snsTagJpaEntities.add(snsTagJpaEntity)
        snsTagJpaEntity.changeSnsJpaEntity(snsJpaEntity = this)
    }

    fun mapDomainEntity(sns: Sns) {
        sns.changeId(id = id!!)
    }

    fun toDomainEntity() =
        Sns(
            id = id!!,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            productIds = productIds.split(",").map { it.toLong() },
            isDisplay = isDisplay,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        )

    fun toDomainEntityAssociatedEntities(snsTagJpaEntities: List<SnsTagJpaEntity>) =
        Sns(
            id = id!!,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            isDisplay = isDisplay,
            productIds = productIds.split(",").map { it.toLong() },
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            snsImages = snsImageJpaEntities.map { it.toDomainEntity() },
            snsTag = snsTagJpaEntities.map { it.toDomainEntity() }.first(),
        )

    fun toDomainEntityAssociatedEntities() =
        Sns(
            id = id!!,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            isDisplay = isDisplay,
            productIds = productIds.split(",").map { it.toLong() },
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            snsImages = snsImageJpaEntities.map { it.toDomainEntity() },
            snsTag = snsTagJpaEntities.map { it.toDomainEntity() }.first(),
        )

    companion object {
        operator fun invoke(sns: Sns) =
            with(receiver = sns) {
                val snsJpaEntity = SnsJpaEntity(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    productIds = productIds.joinToString(","),
                    isDisplay = isDisplay,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
                sns.snsImages.forEach {
                    snsJpaEntity.addSnsImageJpaEntity(snsImageJpaEntity = SnsImageJpaEntity(snsImage = it))
                }
                snsJpaEntity.addSnsTagJpaEntity(snsTagJpaEntity = SnsTagJpaEntity(snsTag = sns.snsTag!!))
                snsJpaEntity
            }
    }
}
