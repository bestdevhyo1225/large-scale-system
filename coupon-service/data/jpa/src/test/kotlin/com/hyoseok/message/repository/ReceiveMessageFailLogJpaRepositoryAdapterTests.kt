package com.hyoseok.message.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.message.entity.ReceiveMessageFailLog
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class ReceiveMessageFailLogJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var receiveMessageFailLogRepository: ReceiveMessageFailLogRepository

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
    }
}
