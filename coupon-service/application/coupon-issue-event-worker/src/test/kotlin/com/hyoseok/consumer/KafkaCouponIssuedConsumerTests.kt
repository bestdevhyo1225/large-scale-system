package com.hyoseok.consumer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.CouponIssuedEventWorker
import com.hyoseok.config.TestKafkaProducer
import com.hyoseok.consumer.dto.CouponIssuedCreateDto
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponIssuedReadRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka

@SpringBootTest(classes = [CouponIssuedEventWorker::class, TestKafkaProducer::class])
@EmbeddedKafka(
    brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"],
    topics = ["coupon-issued-topic"],
    partitions = 1,
)
internal class KafkaCouponIssuedConsumerTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var testKafkaProducer: TestKafkaProducer

    @Autowired
    private lateinit var couponIssuedReadRepository: CouponIssuedReadRepository

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    init {
        this.describe("onMessage 메서드는") {
            context("토픽으로 부터 메시지를 수신 받고") {
                it("쿠폰 발급을 처리한다") {
                    // given
                    val couponIssuedCreateDto = CouponIssuedCreateDto(couponId = 1L, memberId = 1L)
                    val payload: String = jacksonObjectMapper.writeValueAsString(couponIssuedCreateDto)

                    testKafkaProducer.send(payload = payload)

                    // when
                    delay(timeMillis = 1_000) // 1초 동안 대기해야 컨슈머에서 메시지를 수신 받음

                    // then
                    with(receiver = couponIssuedCreateDto) {
                        val couponIssued: CouponIssued = couponIssuedReadRepository.findByCouponIdAndMemberId(
                            couponId = couponId,
                            memberId = memberId,
                        )
                        couponIssued.couponId.shouldBe(couponId)
                        couponIssued.memberId.shouldBe(memberId)
                    }
                }
            }

            context("중복된 메시지를 수신하는 경우") {
                it("쿠폰 발급을 처리하지 않는다") {
                    // given
                    val couponIssuedCreateDto = CouponIssuedCreateDto(couponId = 1L, memberId = 1L)
                    val payload: String = jacksonObjectMapper.writeValueAsString(couponIssuedCreateDto)

                    testKafkaProducer.send(payload = payload)
                    testKafkaProducer.send(payload = payload)

                    // when
                    delay(timeMillis = 1_000) // 1초 동안 대기해야 컨슈머에서 메시지를 수신 받음

                    // then
                    with(receiver = couponIssuedCreateDto) {
                        val couponIssued: CouponIssued = couponIssuedReadRepository.findByCouponIdAndMemberId(
                            couponId = couponId,
                            memberId = memberId,
                        )
                        couponIssued.couponId.shouldBe(couponId)
                        couponIssued.memberId.shouldBe(memberId)
                    }
                }
            }
        }
    }
}
