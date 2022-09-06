package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration
import java.util.Objects

@SpringBootApplication
class QueryWebFluxApplication

fun main(args: Array<String>) {
    runApplication<QueryWebFluxApplication>(*args)
    // 애플리케이션 내의 블로킹 코드롤 체크하고, 블로킹 코드가 존재할 경우 에러를 발생시킨다.
    // 스레드에서 블로킹 콜 여부를 감시하기 때문에 개발 환경이 아닌 상용 환경에서 사용한다면 성능에 영향을 미치게된다.
    // 따라서 개발 환경에서 적용하여 블로킹 콜이 없는지 최대한 테스트 하고, 상용 환경에선 이 라이브러리를 사용하지 않도록 하자.
    enableBlockHoundIfArgsExists(args = args)
}

private const val ARG_ENABLE_BLOCKHOUND = "ENABLE_BLOCKHOUND"

private fun enableBlockHoundIfArgsExists(args: Array<String>) {
    if (Objects.isNull(args)) {
        return
    }

    for (arg in args) {
        if (Objects.nonNull(arg) && arg.contains(ARG_ENABLE_BLOCKHOUND)) {
            return enableBlockHound()
        }
    }
}

private fun enableBlockHound() {
    BlockHound.install(
        object : BlockHoundIntegration {
            override fun applyTo(builder: BlockHound.Builder) {
            }
        },
    )
}
