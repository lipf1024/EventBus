package com.eventbus.one.eventbus

import com.eventbus.one.eventbus.inf.Handle

class Future<T>() : Handle<T> {
    /**
     * Something has happened, so handle it.
     *
     * @param event  the event to handle
     */
    private var mhandle: (T) -> Unit = {}

    override fun handle(event: T) {
        mhandle(event)
    }

    fun applyMethod(handle: (T) -> Unit): Handle<T> {
        this.mhandle = handle
        return this
    }

}