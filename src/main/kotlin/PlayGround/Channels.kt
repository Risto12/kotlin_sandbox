package PlayGround

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel


class GlobalChannelInstance private constructor() {
    companion object {
        private var INSTANCE: Channel<Int>? = null
        fun get(): Channel<Int> {
            if(INSTANCE == null) INSTANCE = Channel()
            return INSTANCE!!
        }
    }
}

suspend fun testChannelProducerConsumer1() = coroutineScope {
    val job1 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting producer")
        repeat(10) {
            val channel = GlobalChannelInstance.get()
            println("Sending")
            channel.send(it)
            delay(500)
        }
        println("Closing producer")
    }
    val job2 = CoroutineScope(Job() + Dispatchers.IO).launch {
        delay(500)
        println("Starting consumer")
        val channel = GlobalChannelInstance.get()
        repeat(10){
            val value = channel.receive()
            println(value)
        }
        println("Closing producer")
    }

    job1.join()
    job2.join()
}