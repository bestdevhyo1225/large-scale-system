package com.hyoseok.member.entity

import java.util.Objects

class Member private constructor(
    id: Long? = null,
    name: String,
) {

    var id: Long? = id
        private set

    var name: String = name
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Member(id=$id, name=$name)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherMember: Member = (other as? Member) ?: return false
        return this.id == otherMember.id &&
            this.name == otherMember.name
    }

    companion object {
        operator fun invoke(name: String) = Member(name = name)
        operator fun invoke(id: Long, name: String) = Member(id = id, name = name)
    }

    fun changeId(id: Long) {
        this.id = id
    }
}
