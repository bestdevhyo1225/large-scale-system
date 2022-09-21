package com.hyoseok.service.feed

import com.hyoseok.config.RedisFeedKeys
import com.hyoseok.config.RedisPostExpireTimes.POST
import com.hyoseok.config.RedisPostKeys
import com.hyoseok.feed.entity.FeedCache
import com.hyoseok.feed.repository.FeedCacheReadRepository
import com.hyoseok.feed.repository.FeedCacheRepository
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
    private val feedCacheRepository: FeedCacheRepository,
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
            val postIds: List<Long> = getFeedCaches(memberId = memberId, start = start, count = count)
                .map { it.postId }

            getPostCaches(postIds = postIds, count = count)
        }

        val member: Member = deferredMember.await()
        val postCaches: List<PostCache> = deferredPostCaches.await()

        postCaches.map { PostFindResultDto.invoke(postCache = it, member = member) }
    }

    private suspend fun getFeedCaches(memberId: Long, start: Long, count: Long): List<FeedCache> {
        val key: String = RedisFeedKeys.getMemberFeedKey(id = memberId)
        val end: Long = start + count
        val feedCaches: List<FeedCache> = feedCacheReadRepository.zrevrange(
            key = key,
            start = start,
            end = end,
            clazz = FeedCache::class.java,
        )

        if (feedCaches.isNotEmpty()) {
            feedCacheRepository.zremRangeByRank(key = key, start = start, end = end)
        }

        return feedCaches
    }

    private suspend fun getPostCaches(postIds: List<Long>, count: Long): List<PostCache> {
        if (postIds.isEmpty()) {
            return listOf()
        }

        val postCaches: List<PostCache> = postCacheReadRepository.mget(
            keys = postIds.map { RedisPostKeys.getPostKey(id = it) },
            clazz = PostCache::class.java,
        )
//        val postViewCounts: List<Long> = postCacheReadRepository.mget(
//            keys = postIds.map { RedisPostKeys.getPostViewsKey(id = it) },
//            clazz = Long::class.java,
//        )

        if (postCaches.isNotEmpty() && postCaches.size.toLong() == count) {
            return postCaches
        }

        val posts: List<Post> = postReadRepository.findAllByIds(ids = postIds)

        CoroutineScope(context = Dispatchers.IO).launch {
            postCacheRepository.setAllUsePipeline(
                keysAndValues = posts.associate { RedisPostKeys.getPostKey(id = it.id!!) to it.toPostCache() },
                expireTime = POST,
                timeUnit = SECONDS,
            )
        }

        return posts.map { it.toPostCache() }
    }

    private suspend fun getMember(memberId: Long): Member = memberReadRepository.findById(id = memberId)
}
