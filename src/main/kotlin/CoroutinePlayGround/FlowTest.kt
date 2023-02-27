package CoroutinePlayGround

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

suspend fun testFlow1() = coroutineScope {
    val flowTest = flow {
        repeat(5) {
           emit(it)
        }
    }
    val j1 = launch {
        delay(1000)
        val name = "job1: "
        flowTest.collect {
            delay(100)
            println(name + it)
        }
    }

    val j2 = launch {
        delay(3000)
        val name = "job2: "
        flowTest.collect {
            delay(100)
            println(name + it)
        }
    }

    j1.join()
    j2.join()
}

private fun flowCreator() = flow<String> {
    while(true) {
        emitAll(listOf("hello", "test", "dah").asFlow())
    }
}

suspend fun testFlowEmitAll() {
    val str = flowCreator()
    val result = flowCreator().first {
        delay(2000)
        println("Value emitted:$it")
        it == "test"
    }
    println("Hello world")
    println(result)
}

suspend fun testFlowWinnerInnerFlow() {
    flow outer@{
        flow {
            emit(2)
        }.collect {
            emit(it + 2)
        }
    }.map {
        it + 2
    }.collect {
        println(it)
    }

}