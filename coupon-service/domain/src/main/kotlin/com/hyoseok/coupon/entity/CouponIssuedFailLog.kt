package com.hyoseok.coupon.entity

import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType
import java.time.LocalDateTime
import java.util.Objects

class CouponIssuedFailLog private constructor(
    id: Long = 0,
    applicationType: CouponIssuedFailLogApplicationType,
    data: String,
    errorMessage: String? = null,
    createdAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var applicationType: CouponIssuedFailLogApplicationType = applicationType
        private set

    var data: String = data
        private set

    var errorMessage: String? = errorMessage
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "CouponIssuedFailLog(id=$id, applicationType=$applicationType, data=$data, errorMessage=$errorMessage, " +
            "createdAt=$createdAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCouponIssuedFailLog: CouponIssuedFailLog = (other as? CouponIssuedFailLog) ?: return false
        return this.id == otherCouponIssuedFailLog.id &&
            this.applicationType == otherCouponIssuedFailLog.applicationType &&
            this.data == otherCouponIssuedFailLog.data &&
            this.errorMessage == otherCouponIssuedFailLog.errorMessage &&
            this.createdAt == otherCouponIssuedFailLog.createdAt
    }

    companion object {
        operator fun invoke(applicationType: String, data: String, errorMessage: String? = null) =
            CouponIssuedFailLog(
                applicationType = CouponIssuedFailLogApplicationType(value = applicationType),
                data = data,
                errorMessage = errorMessage,
                createdAt = LocalDateTime.now().withNano(0),
            )

        operator fun invoke(
            applicationType: CouponIssuedFailLogApplicationType,
            data: String,
            errorMessage: String? = null,
        ) =
            CouponIssuedFailLog(
                applicationType = applicationType,
                data = data,
                errorMessage = errorMessage,
                createdAt = LocalDateTime.now().withNano(0),
            )

        operator fun invoke(
            id: Long,
            applicationType: CouponIssuedFailLogApplicationType,
            data: String,
            errorMessage: String?,
            createdAt: LocalDateTime,
        ) = CouponIssuedFailLog(
            id = id,
            applicationType = applicationType,
            data = data,
            errorMessage = errorMessage,
            createdAt = createdAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}
