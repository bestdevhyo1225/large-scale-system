package com.hyoseok.config.datasource.replication

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class ReplicationRoutingDataSource : AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): ReplicationDataSourceType {
        return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            ReplicationDataSourceType.READ
        } else {
            ReplicationDataSourceType.WRITE
        }
    }
}
