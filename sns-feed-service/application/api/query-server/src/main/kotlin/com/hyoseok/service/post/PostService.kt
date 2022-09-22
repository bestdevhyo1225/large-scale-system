package com.hyoseok.service.post

import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.post.entity.Post
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.service.dto.PostFindResultDto
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class PostService(
    private val memberReadRepository: MemberReadRepository,
    private val postReadRepository: PostReadRepository,
) {

    fun find(memberId: Long, start: Long, count: Long): Pair<Long, List<PostFindResultDto>> = runBlocking {
        val deferredMember: Deferred<Member> = async(context = Dispatchers.IO) {
            getMember(memberId = memberId)
        }
        val deferredPosts: Deferred<Pair<Long, List<Post>>> = async(context = Dispatchers.IO) {
            getPosts(memberId = memberId, start = start, count = count)
        }

        val member: Member = deferredMember.await()
        val (postTotal: Long, posts: List<Post>) = deferredPosts.await()

        Pair(
            first = postTotal,
            second = posts.map { PostFindResultDto(postCache = it.toPostCache(), member = member) },
        )
    }

    private suspend fun getMember(memberId: Long): Member = memberReadRepository.findById(id = memberId)

    private suspend fun getPosts(memberId: Long, start: Long, count: Long): Pair<Long, List<Post>> =
        postReadRepository.findRecentlyRegisteredAllByMemberIdAndPage(
            memberId = memberId,
            limit = count,
            offset = start,
        )
}
