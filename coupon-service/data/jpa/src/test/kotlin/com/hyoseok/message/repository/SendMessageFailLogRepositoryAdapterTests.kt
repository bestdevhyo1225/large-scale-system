package com.hyoseok.message.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.message.entity.SendMessageFailLog
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class SendMessageFailLogRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var sendMessageFailLogRepository: SendMessageFailLogRepository

    @Autowired
    private lateinit var sendMessageFailLogReadRepository: SendMessageFailLogReadRepository

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

        this.describe("findAllByLimitAndOffset 메서드는") {
            it("전체 SendMessageFailLog 전체 수와 리스트를 반환한다") {
                // given
                val instanceId = "Producer-Api"
                val data = "data"
                val errorMessage = "ErrorMessage"
                val sendMessageFailLog =
                    SendMessageFailLog(instanceId = instanceId, data = data, errorMessage = errorMessage)

                sendMessageFailLogRepository.save(sendMessageFailLog = sendMessageFailLog)

                // when
                val result: Pair<Long, List<SendMessageFailLog>> =
                    sendMessageFailLogReadRepository.findAllByLimitAndOffset(limit = 10, offset = 0)

                // then
                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }

        this.describe("findAllByInstanceIdAndLimitAndOffset 메서드는") {
            it("instanceId 기준으로 SendMessageFailLog 전체 수와 리스트를 반환한다") {
                // given
                val instanceId = "Producer-Api"
                val data = "data"
                val errorMessage = "ErrorMessage"
                val sendMessageFailLog =
                    SendMessageFailLog(instanceId = instanceId, data = data, errorMessage = errorMessage)

                sendMessageFailLogRepository.save(sendMessageFailLog = sendMessageFailLog)

                // when
                val result: Pair<Long, List<SendMessageFailLog>> =
                    sendMessageFailLogReadRepository.findAllByInstanceIdAndLimitAndOffset(
                        instanceId = instanceId,
                        limit = 10,
                        offset = 0,
                    )

                // then
                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }
    }
}
