package com.hyoseok.message.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.message.entity.ReceiveMessageFailLog
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

internal class ReceiveMessageFailLogJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var receiveMessageFailLogRepository: ReceiveMessageFailLogRepository

    @Autowired
    private lateinit var receiveMessageFailLogReadRepository: ReceiveMessageFailLogReadRepository

    init {
        this.describe("save 메서드는") {
            it("ReceiveMessageFailLog 엔티티를 저장한다") {
                // given
                val instanceId = "Consumer-Api"
                val data = "data"
                val errorMessage = "ErrorMessage"
                val useRetry = false
                val receiveMessageFailLog = ReceiveMessageFailLog(
                    instanceId = instanceId,
                    data = data,
                    errorMessage = errorMessage,
                    useRetry = useRetry,
                )

                // when
                receiveMessageFailLogRepository.save(receiveMessageFailLog = receiveMessageFailLog)

                // then
                receiveMessageFailLog.id.shouldNotBeNull()
                receiveMessageFailLog.id.shouldNotBeZero()
            }
        }

        this.describe("findAllByLimitAndOffset 메서드는") {
            it("전체 ReceiveMessageFailLog 전체 수와 리스트를 반환한다") {
                // given
                val instanceId = "Consumer-Api"
                val data = "data"
                val errorMessage = "ErrorMessage"
                val useRetry = false
                val receiveMessageFailLog = ReceiveMessageFailLog(
                    instanceId = instanceId,
                    data = data,
                    errorMessage = errorMessage,
                    useRetry = useRetry,
                )

                receiveMessageFailLogRepository.save(receiveMessageFailLog = receiveMessageFailLog)

                // when
                val result: Pair<Long, List<ReceiveMessageFailLog>> =
                    receiveMessageFailLogReadRepository.findAllByLimitAndOffset(limit = 10, offset = 0)

                // then
                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }

        this.describe("findAllByInstanceIdAndLimitAndOffset 메서드는") {
            it("instanceId 기준으로 ReceiveMessageFailLog 전체 수와 리스트를 반환한다") {
                // given
                val instanceId = "Consumer-Api"
                val data = "data"
                val errorMessage = "ErrorMessage"
                val useRetry = false
                val receiveMessageFailLog = ReceiveMessageFailLog(
                    instanceId = instanceId,
                    data = data,
                    errorMessage = errorMessage,
                    useRetry = useRetry,
                )

                receiveMessageFailLogRepository.save(receiveMessageFailLog = receiveMessageFailLog)

                // when
                val result: Pair<Long, List<ReceiveMessageFailLog>> =
                    receiveMessageFailLogReadRepository.findAllByInstanceIdAndLimitAndOffset(
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
