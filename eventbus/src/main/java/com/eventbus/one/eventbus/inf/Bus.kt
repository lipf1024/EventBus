package com.eventbus.one.eventbus.inf

import com.eventbus.one.eventbus.Busimp

interface Bus {
    companion object {
        val bus:Bus = Busimp()
    }
    fun refresh():Bus
    fun sentwithCache(address: String, message: Message):Bus
    fun sent(address: String, message: Message):Bus
    fun register(address: String, handle: Handle<Message>): MessageConsumer
    fun register(address: String,handle:(Message)->Unit):MessageConsumer
    fun register(address: String): MessageConsumer
    fun onceregister(address: String, handle: Handle<Message>):Bus
    fun onceregister(address: String,handle:(Message)->Unit):Bus
    fun unregister(consumer: MessageConsumer): Bus
    /*
    添加一次使用处理器
     */
}