package com.hyoseok.coupon.integration

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.CouponCommandApiApplication
import com.hyoseok.coupon.config.RedisCouponEmbbededServerConfig
import com.hyoseok.coupon.controller.request.CouponCreateRequest
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest(classes = [CouponCommandApiApplication::class, RedisCouponEmbbededServerConfig::class])
@DirtiesContext
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CouponIntegrationTests : DescribeSpec() {

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
                    val resultActions: ResultActions = mockMvc.perform(
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
                }
            }
        }
    }
}
