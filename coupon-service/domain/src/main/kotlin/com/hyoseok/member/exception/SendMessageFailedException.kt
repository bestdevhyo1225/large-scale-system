package com.hyoseok.member.exception

class SendMessageFailedException(val instanceId: String, message: String) : RuntimeException(message)
