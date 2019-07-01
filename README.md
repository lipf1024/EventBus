# 基于kotlin协程的异步EventBus

## 引入
```gradle
    //引入对kotlin协程的支持
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.0'
    //引入eventbus
    implementation 'com.fly:eventbus:1.0.0'
```
## 使用方法
   该项目基于kotlin协程的特性异步发布与通知
## 消息创建与读取
Message类可进行链式操作
### 消息创建
```kotlin
var msg=Message.Creator().setMsg("holloworld")
```
### 消息读取
```kotlin
var hollo=msg.body<String>()
```
## 处理器注册
### 直接注册处理器方式
该注册方式注册的事件处理器当不需要使用时需要注销处理器
```kotlin
//注册处理器
var handle=Bus.getBus.register("address"){ msg->
           //Do you want to do
        }
//注销处理器
 handle.unregister()
```
### 间接处理器注册方式
该注册方式注册的事件处理器当不需要使用时需要注销处理器
```kotlin
  //注册处理器地址
  var handle=Bus.getBus.register("address")
  //注册处理器
  handle1.handle { 
            //Do you want to do 
        }
  //注销处理器
  handle.unregister()
```
### 一次性处理器注册
该种方式注册的处理器在第一次触发后自动注销
```kotlin
Bus.getBus.onceregister("address"){msg->
          //Do you want to do   
        }
```
## 消息发布
### 无缓存式的消息发布
使用该模式进行消息发布时，若当前没有对应的处理器被注册则丢弃消息。
```kotlin
Bus.getBus.sent("address", Message.Creator().setMsg("HolloWorld"))
```
### 带缓存式的消息发布
使用该模式进行消息发布时，若当前没有对应的处理器则缓存消息，等待对应的处理器被注册。
```kotlin
 Bus.getBus.sentwithCache("address", Message.Creator().setMsg("HolloWorld"))
```





