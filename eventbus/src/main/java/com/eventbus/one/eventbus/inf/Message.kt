package com.eventbus.one.eventbus.inf

import com.eventbus.one.eventbus.Messageimp

interface Message {
    companion object {
        fun Creator(): Message {
            return Messageimp()
        }
    }

    fun <T> body(): T?
    fun <T> setMsg(msg: T): Message
}