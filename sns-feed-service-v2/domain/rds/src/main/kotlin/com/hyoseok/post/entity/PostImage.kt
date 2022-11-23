package com.hyoseok.post.entity

import org.hibernate.annotations.DynamicUpdate
import java.util.Objects
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
@Table(name = "post_image")
@DynamicUpdate
class PostImage private constructor(
    url: String,
    sortOrder: Int,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "url", nullable = false)
    var url: String = url
        protected set

    @Column(name = "sort_order", nullable = false)
    var sortOrder: Int = sortOrder
        protected set

    // 외래키를 사용하지 않고, '@JoinColumn' 은 엔티티 간 조인과 관계없이 외래키 이름 지정을 위해서만 사용하는 것이다.
    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post? = null
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherPostImage: PostImage = (other as? PostImage) ?: return false
        return this.id == otherPostImage.id
    }

    override fun toString() = "PostImage(id=$id, url=$url, sortOrder=$sortOrder)"

    companion object {
        operator fun invoke(url: String, sortOrder: Int): PostImage = PostImage(url = url, sortOrder = sortOrder)
    }

    fun changePost(post: Post) {
        this.post = post
    }
}
