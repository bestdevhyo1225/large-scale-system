package com.hyoseok.base.entity

import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "child")
@IdClass(value = ChildPk::class)
class Child private constructor(
    id: Long,
) {

    @Id
    @Column(name = "child_id")
    var id: Long = id
        protected set

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Parent? = null
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherChild: Child = (other as? Child) ?: return false
        return this.id == otherChild.id &&
            this.parent == otherChild.parent
    }

    companion object {
        operator fun invoke(id: Long): Child = Child(id = id)
    }

    fun changeParent(parent: Parent) {
        this.parent = parent
    }
}
