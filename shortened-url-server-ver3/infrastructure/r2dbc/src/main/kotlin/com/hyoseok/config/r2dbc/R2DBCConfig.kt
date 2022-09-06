package com.hyoseok.config.r2dbc

import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories(basePackages = ["com.hyoseok.repository"])
class R2DBCConfig
