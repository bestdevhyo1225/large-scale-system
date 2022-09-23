package com.hyoseok.service.feed

import com.hyoseok.config.RedisExpireTimes.POST
import com.hyoseok.config.RedisKeys
import com.hyoseok.feed.entity.FeedCache
import com.hyoseok.feed.repository.FeedCacheReadRepository
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.repository.PostCacheReadRepository
import com.hyoseok.post.repository.PostCacheRepository
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.service.dto.PostFindResultDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.SECONDS

@Service
class FeedService(
    private val feedCacheReadRepository: FeedCacheReadRepository,
    private val memberReadRepository: MemberReadRepository,
    private val postCacheRepository: PostCacheRepository,
    private val postCacheReadRepository: PostCacheReadRepository,
    private val postReadRepository: PostReadRepository,
) {

    fun find(memberId: Long, start: Long, count: Long): List<PostFindResultDto> = runBlocking {
        val deferredMember: Deferred<Member> = async(context = Dispatchers.IO) {
            getMember(memberId = memberId)
        }
        val deferredPostCaches: Deferred<List<PostCache>> = async(context = Dispatchers.IO) {
            getPostCaches(postIds = getFeedCaches(memberId = memberId, start = start, count = count).map { it.postId })
        }

        val member: Member = deferredMember.await()
        val postCaches: List<PostCache> = deferredPostCaches.await()

        postCaches.map { PostFindResultDto(postCache = it, member = member) }
    }

    private suspend fun getFeedCaches(memberId: Long, start: Long, count: Long): List<FeedCache> {
        return feedCacheReadRepository.zrevrange(
            key = RedisKeys.getMemberFeedKey(id = memberId),
            start = start,
            end = start.plus(count).minus(1),
            clazz = FeedCache::class.java,
        )
    }

    private suspend fun getPostCaches(postIds: List<Long>): List<PostCache> {
        if (postIds.isEmpty()) {
            return listOf()
        }

        val postCaches: List<PostCache> = postCacheReadRepository.mget(
            keys = postIds.map { RedisKeys.getPostKey(id = it) },
            clazz = PostCache::class.java,
        )
//        val postViewCounts: List<Long> = postCacheReadRepository.mget(
//            keys = postIds.map { RedisPostKeys.getPostViewsKey(id = it) },
//            clazz = Long::class.java,
//        )

        if (postCaches.isNotEmpty()) {
            return postCaches
        }

        val posts: List<Post> = postReadRepository.findRecentlyRegisteredAllByIds(ids = postIds)

        CoroutineScope(context = Dispatchers.IO).launch {
            postCacheRepository.setAllUsePipeline(
                keysAndValues = posts.associate { RedisKeys.getPostKey(id = it.id!!) to it.toPostCache() },
                expireTime = POST,
                timeUnit = SECONDS,
            )
        }

        return posts.map { it.toPostCache() }
    }

    private suspend fun getMember(memberId: Long): Member = memberReadRepository.findById(id = memberId)
}
