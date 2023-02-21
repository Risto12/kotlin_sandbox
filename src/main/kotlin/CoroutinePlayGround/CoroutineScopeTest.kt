import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// coroutineScope block the suspending function where it was called until all of its children's has ended
suspend fun coroutineScopeTest1() = coroutineScope {
    println("Start")
    launch {
        delay(2000)
        println("Hello 1")
    }
    launch {
        delay(1000)
        println("Hello 3")
    }
    println("End")
}

// Just async version of above
suspend fun coroutineScopeTest1Async() = coroutineScope {
    val a1 = async {
        delay(2000)
        "Async hello1"
    }

    val a2 = async {
        delay(1000)
        "Async hello2"
    }

    val v1 = a1.await()
    val v2 = a2.await()
    println(v1)
    println(v2)
    println("Async hello3")
}