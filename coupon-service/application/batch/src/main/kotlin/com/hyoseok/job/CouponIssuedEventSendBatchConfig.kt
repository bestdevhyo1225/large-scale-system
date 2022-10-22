package com.hyoseok.job

import com.hyoseok.config.JobPrefixNames.COUPON_ISSUED_EVENT_SEND
import com.hyoseok.coupon.entity.CouponIssuedLog
import com.hyoseok.coupon.entity.CouponIssuedLogEntity
import com.hyoseok.coupon.repository.CouponIssuedLogRepository
import com.hyoseok.coupon.service.CouponMessageBrokerProducer
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class CouponIssuedEventSendBatchConfig(
    @Value("\${spring.batch.chunk-size:1000}")
    private val chunkSize: Int,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val couponMessageBrokerProducer: CouponMessageBrokerProducer,
    private val couponIssuedLogRepository: CouponIssuedLogRepository,
) {

    data class CouponIssuedCreateDto(val memberId: Long, val couponId: Long)

    @Bean(name = ["${COUPON_ISSUED_EVENT_SEND}Job"])
    fun job(): Job =
        jobBuilderFactory.get("${COUPON_ISSUED_EVENT_SEND}Job")
            .preventRestart()
            .start(step())
            .build()

    @Bean(name = ["${COUPON_ISSUED_EVENT_SEND}Step"])
    fun step(): Step =
        stepBuilderFactory.get("${COUPON_ISSUED_EVENT_SEND}Step")
            .chunk<CouponIssuedLogEntity, CouponIssuedLog>(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()

    @Bean(name = ["${COUPON_ISSUED_EVENT_SEND}Reader"])
    fun reader(): JpaPagingItemReader<CouponIssuedLogEntity> =
        JpaPagingItemReaderBuilder<CouponIssuedLogEntity>()
            .name("${COUPON_ISSUED_EVENT_SEND}Reader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString("SELECT cil FROM CouponIssuedLogEntity cil WHERE cil.isSendCompleted = false")
            .build()

    @Bean(name = ["${COUPON_ISSUED_EVENT_SEND}Processor"])
    fun processor(): ItemProcessor<CouponIssuedLogEntity, CouponIssuedLog> =
        ItemProcessor { it.toDomain() }

    @Bean(name = ["${COUPON_ISSUED_EVENT_SEND}Writer"])
    fun writer(): ItemWriter<CouponIssuedLog> =
        ItemWriter {
            it.forEach { couponIssuedLog ->
                with(receiver = couponIssuedLog) {
                    couponMessageBrokerProducer.send(
                        CouponIssuedCreateDto(
                            memberId = memberId,
                            couponId = couponId,
                        ),
                    )
                    couponIssuedLogRepository.updateIsSendCompleted(
                        couponId = couponId,
                        memberId = memberId,
                        isSendCompleted = true,
                    )
                }
            }
        }
}
