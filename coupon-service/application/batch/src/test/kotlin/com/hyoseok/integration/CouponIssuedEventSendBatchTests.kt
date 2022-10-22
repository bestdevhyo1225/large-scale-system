package com.hyoseok.integration

import com.hyoseok.config.BatchConfig
import com.hyoseok.config.KafkaProducerConfig
import com.hyoseok.config.datasource.DataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.coupon.entity.CouponIssuedLog
import com.hyoseok.coupon.publisher.KafkaCouponProducer
import com.hyoseok.coupon.publisher.KafkaCouponProducerCallback
import com.hyoseok.coupon.repository.CouponIssuedLogJpaReadRepositoryAdapter
import com.hyoseok.coupon.repository.CouponIssuedLogJpaRepositoryAdapter
import com.hyoseok.coupon.repository.CouponIssuedLogReadRepository
import com.hyoseok.coupon.repository.CouponIssuedLogRepository
import com.hyoseok.job.CouponIssuedEventSendBatchConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import java.time.LocalDateTime

@SpringBatchTest
@SpringBootTest(
    classes = [
        BatchConfig::class,
        DataSourceConfig::class,
        JpaConfig::class,
        JpaQueryFactoryConfig::class,
        KafkaProducerConfig::class,
        KafkaCouponProducer::class,
        KafkaCouponProducerCallback::class,
        CouponIssuedEventSendBatchConfig::class,
        CouponIssuedLogJpaRepositoryAdapter::class,
        CouponIssuedLogJpaReadRepositoryAdapter::class,
    ],
)
@EmbeddedKafka(
    brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"],
    topics = ["coupon-issued-topic"],
    partitions = 1,
)
internal class CouponIssuedEventSendBatchTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    private lateinit var couponIssuedLogRepository: CouponIssuedLogRepository

    @Autowired
    private lateinit var couponIssuedLogReadRepository: CouponIssuedLogReadRepository

    init {
        this.beforeSpec {
            val couponId = 1L
            val instanceId = "producer-instance-id"
            val couponIssuedLogs: List<CouponIssuedLog> = (1L..3L).map {
                CouponIssuedLog(
                    couponId = couponId,
                    memberId = it,
                    instanceId = instanceId,
                )
            }
            withContext(context = Dispatchers.IO) {
                couponIssuedLogRepository.saveAll(couponIssuedLogs = couponIssuedLogs)
            }
        }

        this.afterSpec {
            withContext(context = Dispatchers.IO) {
                couponIssuedLogRepository.deleteAllByCreatedAtBefore(createdAt = LocalDateTime.now())
            }
        }

        this.describe("couponIssuedSendEvent 배치 작업은") {
            it("전송 실패한 메시지를 다시 전송한다") {
                // given
                val expectedCouponIssuedLogEntitiesSize = 3

                // when
                val jobExecution: JobExecution = jobLauncherTestUtils.launchJob()

                // then
                jobExecution.status.shouldBe(BatchStatus.COMPLETED)
                jobExecution.exitStatus.shouldBe(ExitStatus.COMPLETED)

                val result: Pair<Long, List<CouponIssuedLog>> =
                    couponIssuedLogReadRepository.findAllByLimitAndOffset(limit = 10, offset = 0)

                result.first.shouldNotBeZero()
                result.first.shouldBe(expectedCouponIssuedLogEntitiesSize)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(expectedCouponIssuedLogEntitiesSize)
                result.second.forEach {
                    it.isSendCompleted.shouldBeTrue()
                    it.sendCompletedAt.shouldNotBeNull()
                }
            }
        }
    }
}
