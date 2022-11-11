// package com.hyoseok.listener
//
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
// import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
// import com.hyoseok.SnsFeedEventWorkerApplication
// import com.hyoseok.config.FeedKafkaProducer
// import com.hyoseok.listener.dto.FeedEventDto
// import io.kotest.core.extensions.Extension
// import io.kotest.core.spec.IsolationMode
// import io.kotest.core.spec.style.DescribeSpec
// import io.kotest.extensions.spring.SpringExtension
// import kotlinx.coroutines.delay
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.kafka.test.context.EmbeddedKafka
//
// @SpringBootTest(classes = [SnsFeedEventWorkerApplication::class, FeedKafkaProducer::class])
// @EmbeddedKafka(
//    brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"],
//    topics = ["feed-topic"],
//    partitions = 1,
// )
// internal class FeedKafkaListenerTests : DescribeSpec() {
//
//    override fun extensions(): List<Extension> = listOf(SpringExtension)
//    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
//
//    @Autowired
//    private lateinit var feedKafkaProducer: FeedKafkaProducer
//
//    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
//
//    init {
//        this.describe("onMessage 메서드는") {
//            context("토픽으로 부터 메시지를 수신 받고") {
//                it("피드 정보를 처리한다") {
//                    // given
//                    val feedEventDto = FeedEventDto(postId = 1L, followerId = 1L)
//                    val payload: String = jacksonObjectMapper.writeValueAsString(feedEventDto)
//
//                    feedKafkaProducer.send(payload = payload)
//
//                    // when
//                    delay(timeMillis = 1_000) // 1초 동안 대기해야 컨슈머에서 메시지를 수신 받음
//
//                    // then
//                }
//            }
//        }
//    }
// }
