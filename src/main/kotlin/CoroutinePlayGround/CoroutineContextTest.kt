package CoroutinePlayGround

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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
