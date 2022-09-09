package com.hyoseok.repository

import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.config.mysql.BasicDataSourceConfig
import com.hyoseok.repository.sns.SnsJpaRepository
import com.hyoseok.repository.sns.SnsRepositoryImpl
import com.hyoseok.repository.sns.read.SnsReadRepositoryImpl
import com.hyoseok.sns.repository.SnsRepository
import com.hyoseok.sns.repository.read.SnsReadRepository
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        JpaQueryFactoryConfig::class,
        SnsJpaRepository::class,
        SnsRepository::class,
        SnsReadRepository::class,
        SnsRepositoryImpl::class,
        SnsReadRepositoryImpl::class,
    ],
)
interface RepositoryImplTests
