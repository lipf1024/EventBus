package com.eventbus.one.eventbus.inf

interface MessageConsumer {
    fun handle(handle: Handle<Message>):MessageConsumer
    fun handle(handle:(Message)->Unit):MessageConsumer
    fun unregister()
}