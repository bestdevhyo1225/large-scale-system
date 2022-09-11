package com.hyoseok.sns.entity

import com.hyoseok.exception.Message.EXCEEDS_SNS_TAGS_SIZE
import com.hyoseok.sns.entity.enums.SnsTagType
import java.util.Objects

class SnsTag private constructor(
    id: Long? = null,
    type: SnsTagType,
    values: List<String>,
) {

    var id: Long? = id
        private set

    var type: SnsTagType = type
        private set

    var values: List<String> = values
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "SnsTag(id=$id, type=$type, values=$values)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherSnsTag = (other as? SnsTag) ?: return false
        return this.id == otherSnsTag.id &&
            this.type == otherSnsTag.type &&
            this.values == otherSnsTag.values
    }

    fun validateValues() {
        if (values.size > MAX_VALUE_SIZE) {
            throw IllegalArgumentException(EXCEEDS_SNS_TAGS_SIZE)
        }
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        private const val MAX_VALUE_SIZE = 3

        operator fun invoke(type: String, values: List<String>) =
            SnsTag(type = SnsTagType(value = type), values = values).also {
                it.validateValues()
            }

        operator fun invoke(id: Long, type: String, values: List<String>) =
            SnsTag(id = id, type = SnsTagType(value = type), values = values).also {
                it.validateValues()
            }

        operator fun invoke(id: Long, type: SnsTagType, values: List<String>) =
            SnsTag(id = id, type = type, values = values)
    }
}
