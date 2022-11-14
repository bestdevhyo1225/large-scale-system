package com.hyoseok.follow.service

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.entity.Follow.Companion.INFLUENCER_FIND_MAX_LIMIT
import com.hyoseok.follow.repository.FollowReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FollowReadService(
    private val followReadRepository: FollowReadRepository,
) {

    fun findFollowerIds(followeeId: Long, limit: Long, offset: Long): Pair<Long, List<Long>> {
        // 10,000 이상의 팔로워를 가진 회원들은 이벤트 발행을 하지 않겠다.
        // 게시물 하나를 등록할 때마다 이벤트 발행하는 프로세스의 부하가 심하다.
        if (followReadRepository.countByFolloweeId(followeeId = followeeId) >= Follow.INFLUENCER_CHECK_COUNT) {
            return Pair(first = 0L, second = listOf())
        }

        val (total: Long, followers: List<Follow>) = followReadRepository.findAllByFolloweeIdAndLimitAndOffset(
            followeeId = followeeId,
            limit = limit,
            offset = offset,
        )

        return Pair(first = total, second = followers.map { it.followerId })
    }

    fun findFolloweeIds(followerId: Long, limit: Long, offset: Long): Pair<Long, List<Long>> {
        if (limit == 0L || offset <= -1) {
            return Pair(first = 0L, second = listOf())
        }

        val (total: Long, followees: List<Follow>) = followReadRepository.findAllByFollowerIdAndLimitAndOffset(
            followerId = followerId,
            limit = limit,
            offset = offset,
        )

        return Pair(first = total, second = followees.map { it.followeeId })
    }

    fun findFolloweeIdsByStaticLimit(followerId: Long): List<Long> =
        followReadRepository
            .findAllByFollowerIdAndLimitOrderByIdDesc(followerId = followerId, limit = INFLUENCER_FIND_MAX_LIMIT)
            .map { it.followeeId }
}
