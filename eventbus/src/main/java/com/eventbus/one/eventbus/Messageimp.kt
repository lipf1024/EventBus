package com.eventbus.one.eventbus

import com.eventbus.one.eventbus.inf.Message


class Messageimp: Message {
    private var msg:Any?=null
    override fun <T> body(): T ?{
        return msg as T
    }
    override fun<T> setMsg(msg:T):Message {
       this.msg=msg
        return this
    }
}