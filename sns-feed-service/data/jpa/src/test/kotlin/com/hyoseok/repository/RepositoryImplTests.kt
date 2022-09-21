package com.hyoseok.repository

import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.config.mysql.BasicDataSourceConfig
import com.hyoseok.follow.repository.FollowReadRepository
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.member.repository.MemberRepository
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.repository.follow.FollowJpaRepository
import com.hyoseok.repository.follow.FollowReadRepositoryImpl
import com.hyoseok.repository.follow.FollowRepositoryImpl
import com.hyoseok.repository.member.MemberJpaRepository
import com.hyoseok.repository.member.MemberReadRepositoryImpl
import com.hyoseok.repository.member.MemberRepositoryImpl
import com.hyoseok.repository.post.PostJpaRepository
import com.hyoseok.repository.post.PostReadRepositoryImpl
import com.hyoseok.repository.post.PostRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        JpaQueryFactoryConfig::class,
        PostJpaRepository::class,
        PostRepository::class,
        PostReadRepository::class,
        PostRepositoryImpl::class,
        PostReadRepositoryImpl::class,
        FollowJpaRepository::class,
        FollowRepository::class,
        FollowReadRepository::class,
        FollowRepositoryImpl::class,
        FollowReadRepositoryImpl::class,
        MemberJpaRepository::class,
        MemberRepository::class,
        MemberReadRepository::class,
        MemberRepositoryImpl::class,
        MemberReadRepositoryImpl::class,
    ],
)
interface RepositoryImplTests
