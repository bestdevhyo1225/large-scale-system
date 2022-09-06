package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
class QueryWebFluxApplication

fun main(args: Array<String>) {
    // 애플리케이션 내의 블로킹 코드롤 체크하고, 블로킹 코드가 존재할 경우 에러를 발생시킨다.
    BlockHound.install()
    runApplication<QueryWebFluxApplication>(*args)
}
