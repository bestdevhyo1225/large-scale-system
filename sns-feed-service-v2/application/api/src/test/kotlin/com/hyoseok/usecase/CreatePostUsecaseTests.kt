package com.hyoseok.usecase

import com.hyoseok.feed.dto.FeedEventDto
import com.hyoseok.feed.producer.FeedKafkaProducer
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostCreateDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageCacheDto
import com.hyoseok.post.dto.PostImageCreateDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostRedisService
import com.hyoseok.post.service.PostService
import com.hyoseok.usecase.dto.CreatePostUsecaseDto
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

internal class CreatePostUsecaseTests : BehaviorSpec(
    {
        val mockFeedKafkaProducer: FeedKafkaProducer = mockk(relaxed = true)
        val mockFollowReadService: FollowReadService = mockk()
        val mockMemberReadService: MemberReadService = mockk()
        val mockPostService: PostService = mockk()
        val mockPostRedisService: PostRedisService = mockk()
        val createPostUsecase = CreatePostUsecase(
            feedKafkaProducer = mockFeedKafkaProducer,
            followReadService = mockFollowReadService,
            memberReadService = mockMemberReadService,
            postService = mockPostService,
            postRedisService = mockPostRedisService,
        )

        given("게시물을 등록하기 위해 아래와 같은 상황이 주어지면") {
            val createPostUsecaseDto = CreatePostUsecaseDto(
                memberId = 1L,
                title = "게시물 제목",
                contents = "게시물 내용",
                images = listOf(
                    PostImageCreateDto(url = "https://test.image/1", sortOrder = 1),
                ),
            )
            val memberDto = MemberDto(
                id = createPostUsecaseDto.memberId,
                name = "사용자",
                influencer = false,
                createdAt = LocalDateTime.now().withNano(0),
                lastLoginDatetime = LocalDateTime.now().withNano(0),
            )
            val postCreateDto = PostCreateDto(
                memberId = memberDto.id,
                writer = memberDto.name,
                title = createPostUsecaseDto.title,
                contents = createPostUsecaseDto.contents,
                images = createPostUsecaseDto.images,
            )
            val postDto = PostDto(
                id = 1L,
                memberId = createPostUsecaseDto.memberId,
                title = createPostUsecaseDto.title,
                contents = createPostUsecaseDto.contents,
                writer = memberDto.name,
                wishCount = 0L,
                createdAt = LocalDateTime.now().withNano(0),
                updatedAt = LocalDateTime.now().withNano(0),
                images = listOf(
                    PostImageDto(
                        id = 1L,
                        url = createPostUsecaseDto.images.first().url,
                        sortOrder = createPostUsecaseDto.images.first().sortOrder,
                    ),
                ),
            )
            val postCacheDto: PostCacheDto = with(receiver = postDto) {
                PostCacheDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    wishCount = wishCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageCacheDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                )
            }
            val limit = 1_000L
            val lastId = 0L
            val followDtos: List<FollowDto> = listOf(
                FollowDto(
                    id = 1L,
                    followeeId = postDto.memberId,
                    followerId = 3,
                    createdAt = LocalDateTime.now().withNano(0),
                ),
            )
            val feedEventDto = FeedEventDto(postId = postCacheDto.id, followerId = postCacheDto.memberId)

            every { mockMemberReadService.findMember(id = createPostUsecaseDto.memberId) } returns memberDto
            every { mockPostService.create(dto = postCreateDto) } returns postDto
            justRun { mockPostRedisService.createOrUpdate(dto = postCacheDto) }
            every {
                mockFollowReadService.findFollowers(
                    followeeId = postDto.memberId,
                    lastId = lastId,
                    limit = limit,
                )
            } returns followDtos
            justRun { mockFeedKafkaProducer.sendAsync(event = feedEventDto) }

            `when`("게시물을 저장하는데") {
                createPostUsecase.execute(createPostUsecaseDto = createPostUsecaseDto)

                then("이와 관련된 메서드들은 최소 1번씩 호출된다") {
                    verify { mockMemberReadService.findMember(id = createPostUsecaseDto.memberId) }
                    verify { mockPostService.create(dto = postCreateDto) }
                    verify {
                        mockFollowReadService.findFollowers(
                            followeeId = postDto.memberId,
                            lastId = lastId,
                            limit = limit,
                        )
                    }
                }
            }
        }
    },
)
