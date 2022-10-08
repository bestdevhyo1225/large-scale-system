package com.hyoseok.member.integration

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.config.IntegrationTests
import com.hyoseok.member.controller.request.MemberCreateRequest
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
internal class MemberIntegrationTests : IntegrationTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    init {
        this.describe("POST /members") {
            context("회원을 생성하고") {
                it("201 Created 응답을 반환한다") {
                    // given
                    val request = MemberCreateRequest(name = "Jang")

                    // when
                    val resultActions: ResultActions = mockMvc
                        .perform(
                            post("/members")
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
    }
}
