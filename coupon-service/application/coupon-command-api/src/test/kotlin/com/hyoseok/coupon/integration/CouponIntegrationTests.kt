package com.hyoseok.coupon.integration

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.config.IntegrationTests
import com.hyoseok.coupon.controller.request.CouponCreateRequest
import com.hyoseok.coupon.controller.request.CouponIssuedCreateRequest
import com.hyoseok.coupon.service.dto.CouponCreateResultDto
import com.hyoseok.response.SuccessResponse
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@AutoConfigureMockMvc
@EmbeddedKafka(
    brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"],
    topics = ["coupon-issued-topic"],
    partitions = 1,
)
internal class CouponIntegrationTests : IntegrationTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    init {
        this.describe("POST /coupons") {
            context("쿠폰을 생성하고") {
                it("201 Created 응답을 반환한다") {
                    // given
                    val now: LocalDateTime = LocalDateTime.now().withNano(0)
                    val request = CouponCreateRequest(
                        name = "쿠폰1",
                        totalIssuedQuantity = 1_000,
                        issuedStartedAt = now,
                        issuedEndedAt = now.plusDays(5),
                        availableStartedAt = now,
                        availableEndedAt = now.plusMonths(1),
                    )

                    // when
                    val resultActions: ResultActions = mockMvc
                        .perform(
                            post("/coupons")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(jacksonObjectMapper.writeValueAsString(request)),
                        ).andDo(print())

                    // then
                    resultActions
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.status").value("success"))
                }
            }
        }

        this.describe("POST /coupons/{id}/issued") {
            context("회원에게 쿠폰을 발급하고") {
                it("201 Created 응답을 반환한다") {
                    // given
                    val now: LocalDateTime = LocalDateTime.now().withNano(0)
                    val couponCreateRequest = CouponCreateRequest(
                        name = "쿠폰1",
                        totalIssuedQuantity = 1_000,
                        issuedStartedAt = now,
                        issuedEndedAt = now.plusDays(5),
                        availableStartedAt = now,
                        availableEndedAt = now.plusMonths(1),
                    )
                    val couponIssuedCreateRequest = CouponIssuedCreateRequest(memberId = 1)
                    val couponsMvcResult: MvcResult = mockMvc
                        .perform(
                            post("/coupons")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(jacksonObjectMapper.writeValueAsString(couponCreateRequest)),
                        )
                        .andDo(print())
                        .andReturn()
                    val successResponse: SuccessResponse<*> = jacksonObjectMapper.readValue(
                        couponsMvcResult.response.contentAsString,
                        SuccessResponse::class.java,
                    )
                    val data: String = jacksonObjectMapper.writeValueAsString(successResponse.data)
                    val couponCreateResultDto: CouponCreateResultDto =
                        jacksonObjectMapper.readValue(data, CouponCreateResultDto::class.java)

                    // when
                    val resultActions: ResultActions = mockMvc
                        .perform(
                            post("/coupons/${couponCreateResultDto.couponId}/issued")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(jacksonObjectMapper.writeValueAsString(couponIssuedCreateRequest)),
                        )
                        .andDo(print())

                    // then
                    resultActions
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.status").value("success"))
                }
            }
        }
    }
}
