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
    influencer: Boolean,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "influencer", nullable = false)
    var influencer: Boolean = influencer
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun toString(): String = "Member(id=$id, name=$name, influencer=$influencer, " +
        "createdAt=$createdAt, updatedAt=$updatedAt)"

    object ErrorMessage {
        const val MAX_LIMIT = "입력할 수 있는 이름의 길이를 초과했습니다"
        const val NOT_INFLUENCER = "인플루언서가 기준에 충족하지 못하기 때문에 인플루언서 계정으로 변경할 수 없습니다"
        const val NOT_NORMAL = "인플루언서 회원 기준에 충족하기 때문에 일반 계정으로 변경할 수 없습니다"
    }

    companion object {
        private const val MAX_LIMIT_NAME_LENGTH = 20

        operator fun invoke(name: String): Member {
            validateName(name = name)
            val nowDatetime: LocalDateTime = LocalDateTime.now()
            return Member(name = name, influencer = false, createdAt = nowDatetime, updatedAt = nowDatetime)
        }

        private fun validateName(name: String) {
            if (name.length > MAX_LIMIT_NAME_LENGTH) {
                throw IllegalArgumentException(MAX_LIMIT)
            }
        }
    }

    fun isInfluencer(): Boolean = influencer

    public fun changeInfluencer(influencer: Boolean) {
        this.influencer = influencer
    }
}
