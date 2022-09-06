package com.hyoseok.config.mysql.replication

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory
import org.springframework.transaction.support.TransactionSynchronizationManager
import reactor.core.publisher.Mono

class ReplicationRoutingConnectionFactory : AbstractRoutingConnectionFactory() {

    override fun determineCurrentLookupKey(): Mono<Any> {
        return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            Mono.just(ReplicationRoutingConnectionKey.READ)
        } else {
            Mono.just(ReplicationRoutingConnectionKey.WRITE)
        }
    }
}
