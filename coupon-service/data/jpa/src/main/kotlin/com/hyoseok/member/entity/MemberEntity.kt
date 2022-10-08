package com.hyoseok.member.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "member")
class MemberEntity private constructor(
    name: String,
    createdAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    companion object {
        operator fun invoke(member: Member) =
            with(receiver = member) {
                MemberEntity(name = name, createdAt = createdAt, deletedAt = deletedAt)
            }
    }
}
