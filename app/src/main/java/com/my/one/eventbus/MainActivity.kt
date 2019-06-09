package com.my.one.eventbus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.eventbus.one.eventbus.inf.Bus
import com.eventbus.one.eventbus.inf.Message

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Bus.getBus.register("MMM"){ r->
            Log.d("QQQQQ",r.body())
        }
        Bus.getBus.sent("MMM", Message.Creator().setMsg("asdnxccncj"))
    }
}
