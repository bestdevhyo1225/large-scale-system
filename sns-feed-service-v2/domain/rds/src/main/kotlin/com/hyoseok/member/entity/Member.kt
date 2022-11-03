package com.hyoseok.member.entity

import com.hyoseok.base.entity.BaseEntity
import com.hyoseok.member.entity.Member.ErrorMessage.MAX_LIMIT
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "member")
@DynamicUpdate
class Member private constructor(
    name: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    object ErrorMessage {
        const val MAX_LIMIT = "입력할 수 있는 이름의 길이를 초과했습니다"
    }

    companion object {
        private const val MAX_LIMIT_NAME_LENGTH = 20

        operator fun invoke(name: String): Member {
            validateName(name = name)
            val nowDatetime: LocalDateTime = LocalDateTime.now()
            return Member(name = name, createdAt = nowDatetime, updatedAt = nowDatetime)
        }

        private fun validateName(name: String) {
            if (name.length > MAX_LIMIT_NAME_LENGTH) {
                throw IllegalArgumentException(MAX_LIMIT)
            }
        }
    }

    override fun toString(): String = "Member(id=$id, name=$name, createdAt=$createdAt, updatedAt=$updatedAt)"
}
