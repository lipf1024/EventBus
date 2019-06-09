package com.eventbus.one.eventbus

import android.util.Log
import com.eventbus.one.eventbus.inf.Bus
import com.eventbus.one.eventbus.inf.Handle
import com.eventbus.one.eventbus.inf.Message
import com.eventbus.one.eventbus.inf.MessageConsumer
import com.eventbus.one.eventbus.unit.synLinkedList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 主要在于处理线程安全问题
 * 冲突情况
 * 1.消息发送中注销或添加处理器
 *
 */
class Busimp : Bus {
    override fun refresh(): Bus {
        asynSent()
        return this
    }


    //防止无consumer消息被抛弃
    private var msg_holder: HashMap<String, synLinkedList<Message>> = HashMap()
    //防止多个consumer冲突
    private var handle_holder: HashMap<String, synLinkedList<MessageConsumerimp>> = HashMap()
    //此处有问题
    private var wait_Sent = synLinkedList<String>()
    private var wait_Sent_Cache = synLinkedList<String>()
    /*
    互斥变量
     */
    private var isRun = false

    /**
     *
     * 判断消息队列是否正在发送
     * 同一线程中同步发送不同线程中异步发送
     * 无对应处理器时则等待处理器
     */
    override fun sentwithCache(address: String, message: Message): Bus {
        /*
               此处代码可多线程同时执行
                */
        Log.d("MYCONN", "发送中" + msg_holder.size + "　" + handle_holder.size)
        var msg_list = msg_holder.get(address)
        //不丢弃消息
        if (msg_list == null) {
            var msgs = synLinkedList<Message>()
            msgs.add(message)
            msg_holder.put(address, msgs)
            wait_Sent_Cache.add(address)
        } else {
            msg_list.add(message)
            //待发送消息地址
            wait_Sent_Cache.add(address)
        }
        /*
        遍历handle 互斥变量isRun结局线程安全问题
        以下代码为互斥同步不可并发
         */
        asynSent()
        return this
    }

    override fun sent(address: String, message: Message): Bus {
        /*
        此处代码可多线程同时执行
         */
        Log.d("MYCONN", "发送中  " + address + msg_holder.size + "　" + handle_holder.size)
        var msg_list = msg_holder.get(address)
        //为空时丢弃消息
        msg_list?.apply {
            msg_list.add(message)
            //待发送消息地址
            wait_Sent.add(address)
        }
        /*
        遍历handle 互斥变量isRun结局线程安全问题
        以下代码为互斥同步不可并发
         */
        asynSent()

        return this
    }

    /*
       遍历handle 互斥变量isRun结局线程安全问题
       以下代码为互斥同步不可并发
        */
    private fun asynSent() {
        if (!isRun) {
            isRun = !isRun
            //处理wait_Sent
            //size是动态变化的
            while (wait_Sent.size() > 0) {

                //查询第一个地址
                var now_address = wait_Sent.get(0)
                Log.d("MYCONNN", "Wait_Sent  " + wait_Sent.size() + now_address)
                //执行此步时必有msgs可能为空 null的话 表明该地址消息已被丢弃
                var msgs = msg_holder.get(now_address)
                //size动态变化
                while (msgs != null && msgs.size() > 0) {
                    Log.d("MYCONNN", "msgs" + msgs.size())
                    var msg = msgs.get(0)
                    //handle必须放在msgs内部
                    //若hand_list为空 则等待下一次在发送
                    var handle_list = handle_holder.get(now_address)
                    handle_list?.foreach { handle ->
                        Log.d("MYCONNN", "handle" + now_address + "   ")
                        GlobalScope.launch {
                            handle.gethandle()?.handle(msg ?: Message.creat())
                        }
                    }
                    //移除第一个发送过的消息
                    msgs.removeAt(0)
                }
                //第一个待处理地址处理完毕移除第一个地址
                wait_Sent.removeAt(0)
            }
            //处理wait_Sent_Cache
            var index = 0
            //有消息队列且处理器数量大于0
            while (wait_Sent_Cache.size() > 0 && handle_holder.size > 0 && index < wait_Sent_Cache.size()) {
                //查询第一个地址，注意这里有一次性处理器
                var now_address = wait_Sent_Cache.get(index)
                var handle_list = handle_holder.get(now_address)
                //存在处理器
                if (handle_list != null && handle_list.size() > 0) {
                    var msgs = msg_holder.get(now_address)
                    while (msgs!!.size() > 0) {
                        var msg = msgs.get(0)
                        //处理器发送消息
                        handle_list?.foreach { handle ->
                            GlobalScope.launch {
                                handle.gethandle()?.handle(msg ?: Message.creat())

                            }
                        }
                        //处理器发送完毕
                        //移除消息
                        msgs.removeAt(0)
                    }
                    wait_Sent_Cache.removeAt(index)
                } else {
                    //没有该地址对应的处理器
                    //此处index后移
                    index++
                }
            }
            isRun = !isRun
        }
    }

    /**
     * 处理器中不应有耗时任务
     */
    override fun register(address: String, handle: Handle<Message>): MessageConsumer {
        return register(address).handle(handle)
    }

    override fun register(address: String, handle: (Message) -> Unit): MessageConsumer {
        return register(address).handle(handle)


    }

    //注册时检测
    override fun register(address: String): MessageConsumer {
        var consumer = MessageConsumerimp(address)
        if (handle_holder.get(address) != null) {
            handle_holder.getValue(address).add(consumer)
        } else {
//            Log.d("MYCONN", "注册中" + address)
            var handle_list = synLinkedList<MessageConsumerimp>()

            handle_list.add(consumer)
            handle_holder.put(address, handle_list)
            if (msg_holder.get(address) == null) {
                var msg_list = synLinkedList<Message>()
                msg_holder.put(address, msg_list)
        //        Log.d("MYCONN", "注册中msg" + address)
            }

      //      Log.d("MYCONN", "注册中" + msg_holder.size + "　" + handle_holder.size)

        }
        return consumer
    }

    override fun unregister(consumer: MessageConsumer): Bus {
        var mconsumer = consumer as MessageConsumerimp
        var handle_list = handle_holder.get(mconsumer.getID())
        handle_list?.apply {
            handle_list.remove(mconsumer)
            //处理器队列为空
            if (handle_list.size() == 0) {
                handle_holder.remove(mconsumer.getID())
                msg_holder.remove(mconsumer.getID())
            }
        }
        Log.d("MYCONN", "注销中" + msg_holder.size + "　" + handle_holder.size)
        return this
    }

    override fun onceregister(address: String, handle: Handle<Message>): Bus {
        var consumer = register(address)
        var future = Future<Message>()
        future.applyMethod { msg ->
            handle.handle(msg)
            consumer.unregister()
        }
        consumer.handle(future)
        return this
    }

    override fun onceregister(address: String, handle: (Message) -> Unit): Bus {
        var consumer = register(address)
        var mhandle: (Message) -> Unit = { msg ->
            handle(msg)
            consumer.unregister()
        }
        consumer.handle(mhandle)
        return this
    }
}