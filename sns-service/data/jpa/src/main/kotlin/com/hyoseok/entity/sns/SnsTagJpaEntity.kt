package com.hyoseok.entity.sns

import com.hyoseok.sns.entity.SnsTag
import com.hyoseok.sns.entity.enums.SnsTagType
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
@Table(name = "sns_tag")
@DynamicUpdate
class SnsTagJpaEntity private constructor(
    type: String,
    values: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "type", nullable = false)
    var type: String = type
        protected set

    @Column(name = "`values`", nullable = false)
    var values: String = values
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_id", nullable = false)
    var snsJpaEntity: SnsJpaEntity? = null
        protected set

    fun changeSnsJpaEntity(snsJpaEntity: SnsJpaEntity) {
        this.snsJpaEntity = snsJpaEntity
    }

    fun toDomainEntity() = SnsTag(id = id!!, type = SnsTagType(value = type), values = values.split(","))

    fun change(snsTag: SnsTag) {
        this.type = snsTag.type.name
        this.values = snsTag.values.joinToString(",")
    }

    companion object {
        operator fun invoke(snsTag: SnsTag) =
            SnsTagJpaEntity(type = snsTag.type.name, values = snsTag.values.joinToString(","))
    }
}
