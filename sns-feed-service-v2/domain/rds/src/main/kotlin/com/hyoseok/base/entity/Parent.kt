package com.hyoseok.base.entity

import java.util.Objects
import javax.persistence.CascadeType.PERSIST
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "parent")
class Parent private constructor(
    name: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    var id: Long? = null
        protected set

    @Column(name = "name")
    var name: String = name
        protected set

    @OneToMany(mappedBy = "parent", cascade = [PERSIST])
    var childs: MutableList<Child> = mutableListOf()

    override fun hashCode(): Int = Objects.hash(id)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherParent: Parent = (other as? Parent) ?: return false
        return this.id == otherParent.id
    }

    companion object {
        operator fun invoke(name: String): Parent = Parent(name = name)
    }

    fun addChild(child: Child) {
        this.childs.add(child)
        child.changeParent(parent = this)
    }
}
