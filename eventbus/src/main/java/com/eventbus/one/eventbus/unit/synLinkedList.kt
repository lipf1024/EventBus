package com.eventbus.one.eventbus.unit
import android.util.Log
import java.util.*

class synLinkedList<T> {
    val list = LinkedList<T>()
    /**
     * 访问任何一个锁对象时 其余所对象将被阻塞
     */
    @Synchronized
    fun add(e: T): Boolean {
        return list.add(e)
    }

    @Synchronized
    fun add(index: Int, e: T) {
        return list.add(index, e)
    }

    @Synchronized
    fun removeAt(index: Int): T {
        return list.removeAt(index)
    }
    @Synchronized
    fun get(index: Int): T? {
        Log.e("MYCONN", "" + index + "   " + list.size + "  ")
        if (index >= 0 && index < list.size) {
            return list.get(index)
        } else {
            return null
        }
    }

    fun size(): Int {
        return list.size
    }

    @Synchronized
    fun indexOf(e: T?): Int {
        return list.indexOf(e)
    }

    @Synchronized
    fun remove(t: T): Boolean {
        return list.remove(t)
    }

    @Synchronized
    fun foreach(handle: (T) -> Unit) {
        list.forEach { t ->
            handle(t)
        }
    }
}