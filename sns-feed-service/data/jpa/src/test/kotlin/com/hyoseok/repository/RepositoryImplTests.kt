package com.hyoseok.repository

import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.config.mysql.BasicDataSourceConfig
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.repository.post.PostJpaRepository
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
        PostRepositoryImpl::class,
    ],
)
interface RepositoryImplTests
