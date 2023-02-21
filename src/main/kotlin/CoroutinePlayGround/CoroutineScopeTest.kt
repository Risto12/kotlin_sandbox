import kotlinx.coroutines.*


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
    // Deferred is also a job
    val a1: Deferred<String> = async {
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

suspend fun testCoroutineScope() {
    val scope = CoroutineScope(SupervisorJob())
    val job = scope.launch {
        println("Started")
        delay(2000)
        println("Ended")
    }
    job.join()
    println("Ending the parent")
}

/***
 * When job is supervisor job the exception in child job 1 doesn't affect child job 2. When the job is normal job
 * the child job 2 is also cancelled
 */
suspend fun testSuperVisorScope(superVisorJob: Boolean = true) = coroutineScope {
    val job = if(superVisorJob) SupervisorJob() else Job()

    val childJob1 = launch(job) {
        println("Starting job1")
        delay(200)
        throw Exception()
        delay(200)
        println("Ending job1")
    }
    val childJob2 = launch(job) {
        println("Starting job2")
        delay(2000)
        println("Ending job2") // Even though childJob1 threw error this continued because of the supervisor job
    }

    childJob1.join()
    childJob2.join()
    println("Ending parent job")
}

/***
 * If exception is cancellation exception or inherits from cancellation exception only the job that where it has been
 * thrown will be cancelled. It doesn't matter if this supervisor job or not
 */
suspend fun testCancellationException(superVisorJob: Boolean = true) = coroutineScope {
    val job = if(superVisorJob) SupervisorJob() else Job()

    val childJob1 = launch(job) {
        println("Starting job1")
        delay(200)
        throw object : CancellationException() { }
        delay(200) // Will not be called
        println("Ending job1")
    }
    val childJob2 = launch(job) {
        println("Starting job2")
        delay(2000)
        println("Ending job2") // Will be printed
    }

    childJob1.join()
    childJob2.join()
    println("Ending parent job")
}
