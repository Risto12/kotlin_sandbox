package CoroutinePlayGround

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/* Key points
Context is inherited from the parentt
The parent waits (suspends) until all the children are finished
If parent is cancelled all the children are cancelled also if child is destroyed it
cancels the parent and all other parent childrens
 */

fun testCoroutineContext() {
    // CoroutineContext is "map" of CoroutineContext
    val ctc: CoroutineContext = CoroutineName("Test1")
    // Using class as map reference because these have companion object that are used
    // as keys
    println(ctc[CoroutineName]?.name) // prints Test1

    // Now we can merge them together
}

fun testContextOverriding() {
    val ctc1 = CoroutineName("Child")
    val ctc2 = Job()

    println(ctc2[CoroutineName]?.name) // Null
    println((ctc1 + ctc2)[CoroutineName]?.name) // Child - like a map ...
}

fun testContextPassing() = runBlocking(CoroutineName("Parent")) {
    // "Inherits" from parent but the CoroutineName context is overriden with "Child"
    launch(CoroutineName("Child")) {
        println(coroutineContext[CoroutineName]?.name) // Prints Child
    }

    launch {
        println(coroutineContext[CoroutineName]?.name) // Prints Parent
    }
}

/***
 * job2 has its own job so when its cancelled it won't stop the parent coroutine
 */
suspend fun testJobCancellation1() = coroutineScope {
    println("Parent start")
    launch {
        println("Job1 started")
        delay(3000)
        println("Job1 ended")
    }

    val job2 = launch(Job()) {
        println("Job2 started")
        delay(1000)
        println("Job2 ended")
    }
    delay(500)
    job2.cancel()
    println("Parent end")
}

/**
 *
 */
suspend fun testJobCancellation2() = coroutineScope {
    println("Parent start")
    val job1 = launch {
        println("Job1 started")
        delay(3000)
        println("Job1 ended")
    }

    launch {
        println("Job2 started")
        delay(1000)
        delay(1000)
        println("Job2 ended")
    }
    delay(500)
    job1.cancel()
    println("Job1 cancelled")
    delay(2000)
    println("Parent end")
}

/**
 * Coroutine join example
 */
suspend fun testingCoroutineJoin() = coroutineScope {
    println("Parent start")
    val job1 = launch {
        println("Job1 started")
        delay(4000)
        println("Job1 ended")
    }

    val job2 = launch {
        println("Job2 started")
        delay(1000)
        println("Job2 ended")
    }
    // .join() does the same as .await for async but async should
    // only be used when something needs to be returned
    job2.join() // wait job2 until proceeding to end
    job1.join() // wait job1 until proceeding to end
    println("Parent end")

}
