package com.my.one.eventbus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.eventbus.one.eventbus.inf.Bus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Bus.bus
    }
}
