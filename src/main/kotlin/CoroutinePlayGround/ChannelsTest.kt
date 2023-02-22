package CoroutinePlayGround

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


// Don't know why I created this :D
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
        val channel = GlobalChannelInstance.get()
        repeat(10) {
            println("Sending")
            channel.send(it)
            delay(500)
        }
        channel.close()
        println("Closing producer")
    }
    val job2 = CoroutineScope(Job() + Dispatchers.IO).launch {
        delay(500)
        println("Starting consumer")
        val channel = GlobalChannelInstance.get()
        channel.consumeEach { println(it) }
        println("Closing producer")
    }

    job1.join()
    job2.join()
}

suspend fun testChannelProducerConsumer2() = coroutineScope {
    // automatic closing of channel
    val channel = produce {
        repeat(10) {
            delay(500)
            send(it)
        }
    }

    for(element in channel) {
        println(element)
    }
}

suspend fun testChannelProducerConsumer3() = coroutineScope {
    val channel = Channel<Int>(capacity = 3) // buffer of 3 after that channel needs to wait before it can produce
    val job1 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting producer")
        repeat(10) {
            println("Sending $it")
            channel.send(it)
            delay(100)
        }
        channel.close()
        println("Closing producer")
    }
    val job2 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting consumer")
        channel.consumeEach {
            delay(1000)
            println(it)
        }
        println("Closing consumer")
    }

    job1.join()
    job2.join()
}

suspend fun testChannelProducerConsumer4() = coroutineScope {
    val channel = Channel<Int>(capacity = Channel.RENDEZVOUS) // Needs to wait for consumer
    val job1 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting producer")
        repeat(10) {
            println("Sending $it")
            channel.send(it)
        }
        channel.close()
        println("Closing producer")
    }
    val job2 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting consumer")
        channel.consumeEach {
            delay(1000)
            println(it)
        }
        println("Closing consumer")
    }

    job1.join()
    job2.join()
}

suspend fun testChannelProducerConsumer5() = coroutineScope {
    val channel = Channel<Int>(capacity = Channel.CONFLATED) // Drop oldest
    val job1 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting producer")
        repeat(10) {
            delay(200)
            println("Sending $it")
            channel.send(it)
        }
        channel.close()
        println("Closing producer")
    }

    val job2 = CoroutineScope(Job() + Dispatchers.IO).launch {
        println("Starting consumer")
        channel.consumeEach {
            delay(1000)
            println(it)
        }
        println("Closing consumer")
    }

    job1.join()
    job2.join()
}


private suspend fun pipe(channel: ReceiveChannel<String> ,modifier: String.() -> String): ReceiveChannel<String> = coroutineScope {
    return@coroutineScope produce(capacity = 3) {
        for(item in channel) {
            println("Creating + " + item)
            delay(500)
            val modified = modifier(item)
            send(modified)
        }
    }
}

suspend fun testPipelineChannel() = coroutineScope {
    val orderChannel = produce(capacity = 3) {
        delay(500)
        for(i in listOf("pizza", "spaghetti", "tomato")) {
            println("Sending " + i)
            send(i)
        }
    }
    delay(1000)
    val builder = pipe(orderChannel) { this + "- Status done"}

    for (item in builder) {
        println("Product is ready: "+ item)
    }
}