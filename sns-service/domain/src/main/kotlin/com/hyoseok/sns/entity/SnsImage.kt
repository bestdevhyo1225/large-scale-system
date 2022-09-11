package com.hyoseok.sns.entity

import com.hyoseok.exception.Message.EXCEEDS_SNS_IMAGES_SIZE
import com.hyoseok.utils.Sha256Util
import java.util.Objects

class SnsImage private constructor(
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
    override fun toString(): String = "SnsImage(id=$id, url=$url, sortOrder=$sortOrder)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherSnsImage = (other as? SnsImage) ?: return false
        return this.id == otherSnsImage.id &&
            this.url == otherSnsImage.url &&
            this.sortOrder == otherSnsImage.sortOrder
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        private const val MAX_SNS_IMAGE_SIZE = 10

        fun createSnsImages(snsImages: List<Pair<String, Int>>): List<SnsImage> {
            if (snsImages.size > MAX_SNS_IMAGE_SIZE) {
                throw IllegalArgumentException(EXCEEDS_SNS_IMAGES_SIZE)
            }

            return snsImages.map { SnsImage(url = Sha256Util.encode(value = it.first), sortOrder = it.second) }
        }

        operator fun invoke(id: Long, url: String, sortOrder: Int) = SnsImage(id = id, url = url, sortOrder = sortOrder)
    }
}
