package com.hyoseok.message.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.message.entity.SendMessageFailLog
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class SendMessageFailLogRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var sendMessageFailLogRepository: SendMessageFailLogRepository

    init {
        this.describe("save 메서드는") {
            it("SendMessageFailLog 엔티티를 저장한다") {
                // given
                val instanceId = "Producer-Api"
                val data = "data"
                val errorMessage = "ErrorMessage"
                val sendMessageFailLog =
                    SendMessageFailLog(instanceId = instanceId, data = data, errorMessage = errorMessage)

                // when
                sendMessageFailLogRepository.save(sendMessageFailLog = sendMessageFailLog)

                // then
                sendMessageFailLog.id.shouldNotBeNull()
                sendMessageFailLog.id.shouldNotBeZero()
            }
        }
    }
}
