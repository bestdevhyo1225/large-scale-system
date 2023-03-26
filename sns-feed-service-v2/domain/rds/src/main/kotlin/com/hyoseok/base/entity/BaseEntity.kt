package com.hyoseok.base.entity

import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(
    createdAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherBase: BaseEntity = (other as? BaseEntity) ?: return false
        // '스레드 == 트랜잭션 == 영속성 컨텍스트' 범위내에서는 id 값만 같아도 같은 객체이다. (영속성 컨텍스트는 REPEATABLE READ로 동작한다.)
        return this.id == otherBase.id
    }
}
