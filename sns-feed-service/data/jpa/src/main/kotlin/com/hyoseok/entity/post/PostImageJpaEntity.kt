package com.hyoseok.entity.post

import org.hibernate.annotations.DynamicUpdate
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
class PostImageJpaEntity private constructor(
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var postJpaEntity: PostJpaEntity? = null
        protected set

    fun changePostJpaEntity(postJpaEntity: PostJpaEntity) {
        this.postJpaEntity = postJpaEntity
    }

    companion object {
        operator fun invoke(url: String, sortOrder: Int) = PostImageJpaEntity(url = url, sortOrder = sortOrder)
    }
}
