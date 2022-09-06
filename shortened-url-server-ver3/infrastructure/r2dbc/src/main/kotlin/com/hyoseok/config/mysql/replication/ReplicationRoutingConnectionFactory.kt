package com.hyoseok.config.mysql.replication

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory
import org.springframework.transaction.reactive.TransactionSynchronizationManager
import reactor.core.publisher.Mono

class ReplicationRoutingConnectionFactory : AbstractRoutingConnectionFactory() {

    override fun determineCurrentLookupKey(): Mono<Any> {
        return TransactionSynchronizationManager.forCurrentTransaction().map {
            if (it.isActualTransactionActive && it.isCurrentTransactionReadOnly) {
                ReplicationRoutingConnectionKey.READ
            } else {
                ReplicationRoutingConnectionKey.WRITE
            }
        }
    }
}
