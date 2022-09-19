package com.hyoseok.post.entity

import java.util.Objects

class PostImage private constructor(
    id: Long? = null,
    url: String,
    sortOrder: Int,
) {

    var id: Long? = id
        private set

    var url: String = url
        private set

    var sortOrder: Int = sortOrder
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "PostImage(id=$id, url=$url, sortOrder=$sortOrder)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherPostImage: PostImage = (other as? PostImage) ?: return false
        return this.id == otherPostImage.id &&
            this.url == otherPostImage.url &&
            this.sortOrder == otherPostImage.sortOrder
    }

    companion object {
        operator fun invoke(url: String, sortOrder: Int) = PostImage(url = url, sortOrder = sortOrder)
        operator fun invoke(id: Long, url: String, sortOrder: Int) =
            PostImage(id = id, url = url, sortOrder = sortOrder)
    }
}
