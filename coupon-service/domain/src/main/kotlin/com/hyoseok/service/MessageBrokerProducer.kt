package com.hyoseok.service

interface MessageBrokerProducer {
    fun <T : Any> send(event: T)
    fun <T : Any> sendAsync(event: T)
}
