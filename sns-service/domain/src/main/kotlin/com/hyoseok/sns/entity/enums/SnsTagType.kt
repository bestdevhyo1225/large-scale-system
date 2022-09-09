package com.hyoseok.sns.entity.enums

import com.hyoseok.exception.Message.NOT_EXISTS_TAG_TYPE

enum class SnsTagType(val label: String) {
    STYLE("스타일 태그"),
    TPO("TPO 태그"),
    ;

    companion object {
        operator fun invoke(value: String) =
            try {
                SnsTagType.valueOf(value.uppercase())
            } catch (exception: Exception) {
                throw IllegalArgumentException(NOT_EXISTS_TAG_TYPE)
            }
    }
}
