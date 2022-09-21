package com.hyoseok.entity.member

import com.hyoseok.member.entity.Member
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "member")
@DynamicUpdate
class MemberJpaEntity(
    name: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    companion object {
        operator fun invoke(member: Member) =
            with(receiver = member) {
                MemberJpaEntity(name = name)
            }
    }

    fun mapDomainEntity(member: Member) {
        member.changeId(id = id!!)
    }

    fun toDomainEntity() = Member(id = id!!, name = name)
}
