package com.hyoseok.message.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.message.entity.SendMessageLog
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

internal class SendMessageLogRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var sendMessageLogRepository: SendMessageLogRepository

    @Autowired
    private lateinit var sendMessageLogReadRepository: SendMessageLogReadRepository

    init {
        this.describe("save 메서드는") {
            it("SendMessageLog 엔티티를 저장한다") {
                // given
                val instanceId = "Producer-Api"
                val data = "data"
                val sendMessageLog =
                    SendMessageLog(instanceId = instanceId, data = data)

                // when
                sendMessageLogRepository.save(sendMessageLog = sendMessageLog)

                // then
                sendMessageLog.id.shouldNotBeNull()
                sendMessageLog.id.shouldNotBeZero()
            }
        }

        this.describe("findAllByLimitAndOffset 메서드는") {
            it("전체 SendMessageLog 전체 수와 리스트를 반환한다") {
                // given
                val instanceId = "Producer-Api"
                val data = "data"
                val sendMessageLog = SendMessageLog(instanceId = instanceId, data = data)

                sendMessageLogRepository.save(sendMessageLog = sendMessageLog)

                // when
                val result: Pair<Long, List<SendMessageLog>> =
                    sendMessageLogReadRepository.findAllByLimitAndOffset(limit = 10, offset = 0)

                // then
                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }

        this.describe("findAllByInstanceIdAndLimitAndOffset 메서드는") {
            it("instanceId 기준으로 SendMessageLog 전체 수와 리스트를 반환한다") {
                // given
                val instanceId = "Producer-Api"
                val data = "data"
                val sendMessageLog = SendMessageLog(instanceId = instanceId, data = data)

                sendMessageLogRepository.save(sendMessageLog = sendMessageLog)

                // when
                val result: Pair<Long, List<SendMessageLog>> =
                    sendMessageLogReadRepository.findAllByInstanceIdAndLimitAndOffset(
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
