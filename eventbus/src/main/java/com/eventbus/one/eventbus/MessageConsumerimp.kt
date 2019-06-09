package com.eventbus.one.eventbus

import com.eventbus.one.eventbus.inf.Bus
import com.eventbus.one.eventbus.inf.Handle
import com.eventbus.one.eventbus.inf.Message
import com.eventbus.one.eventbus.inf.MessageConsumer

class MessageConsumerimp(ID: String) : MessageConsumer {
    private var ID: String = ID
    private var handle: Handle<Message>? = null
    fun gethandle(): Handle<Message>? {
        return handle
    }

    override fun handle(mhandle: Handle<Message>): MessageConsumer {
        this.handle = mhandle
        Bus.getBus.refresh()
        return this
    }

    override fun handle(mhandle: (Message) -> Unit): MessageConsumer {
        var future: Future<Message> = Future()
        this.handle = future.applyMethod(mhandle)
        Bus.getBus.refresh()
        return this

    }

    fun getID(): String {
        return ID
    }

    /**
     * 注销处理器
     */
    override fun unregister() {
        Bus.getBus.unregister(this)
    }
}