package com.eventbus.one.eventbus.inf
@FunctionalInterface
interface Handle<E> {
    /**
     * Something has happened, so handle it.
     *
     * @param event  the event to handle
     */
    fun handle(event: E)
}