package com.hyoseok.member.entity

import java.time.LocalDateTime
import java.util.Objects

class Member private constructor(
    id: Long? = null,
    name: String,
    createdAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    var id: Long? = id
        private set

    var name: String = name
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Member(id=$id, name=$name, createdAt=$createdAt, deletedAt=$deletedAt)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherMember: Member = (other as? Member) ?: return false
        return this.id == otherMember.id &&
            this.name == otherMember.name &&
            this.createdAt == otherMember.createdAt &&
            this.deletedAt == otherMember.deletedAt
    }

    companion object {
        operator fun invoke(name: String) = Member(name = name, createdAt = LocalDateTime.now().withNano(0))
        operator fun invoke(id: Long, name: String, createdAt: LocalDateTime, deletedAt: LocalDateTime?) =
            Member(id = id, name = name, createdAt = createdAt, deletedAt = deletedAt)
    }
}
