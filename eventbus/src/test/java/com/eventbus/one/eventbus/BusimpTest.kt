package com.eventbus.one.eventbus

import com.eventbus.one.eventbus.inf.Bus
import org.junit.Test

class BusimpTest {

    @Test
    fun register() {
        Bus.bus.register("2333")
    }
}