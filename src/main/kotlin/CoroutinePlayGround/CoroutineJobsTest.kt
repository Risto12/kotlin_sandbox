package CoroutinePlayGround

import kotlinx.coroutines.*

/* Key points
Context is inherited from the parentt
The parent waits (suspends) until all the children are finished
If parent is cancelled all the children are cancelled also if child is destroyed it
cancels the parent and all other parent childrens
 */
/**
 * This function suspend the coroutine where it was called until this has ended.
 *
 */
suspend fun testJobConcurrency1() = coroutineScope {
    launch {
        println("Job started")
        delay(2000)
        println("Job ended")
    }
    println("Parent ended") // This doesn't wait the job before its printed because join is not used
}

/**
 * Same as job concurrency test 1 but with join statement that make the function wait for the job
 */
suspend fun testJobConcurrency2() = coroutineScope {
    val job = launch {
        println("Job started")
        delay(2000)
        println("Job ended")
    }
    job.join() // same as async.wait but async should be only used when waiting for value to be returned
    println("Parent ended")
}

/**
 * This version launch uses Job() so the structure is not concurrent.
 * So the suspend function will continue after "parent ended" text even if job is still going
 * Example when this is run from main the application will end before job ended is printed
 */
suspend fun testJobConcurrency3() = coroutineScope {
    launch(Job()) {
        println("Job started")
        delay(1000)
        println("Job ended")
    }
    println("Parent ended")
}

/**
 * missing solve
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
 * Testing coroutineContext.job.children property that keeps track of all of the launched
 * coroutines inside this scope/context
 */
suspend fun testJobConcurrency4() = coroutineScope {
    launch {
        println("Job1 started")
        delay(3000)
        println("Job1 ended")
    }

    launch {
        println("job2 started")
        delay(2000)
        println("job2 ended")
    }

    coroutineContext.job.children.forEach {
        it.join()
    }
    println("Parent ended")
}

suspend fun testJobWillNeverEnd(complete: Boolean = false) = coroutineScope {
    val job = Job()
    launch(job) {
        println("Job1 started")
        delay(2000)
        println("Job1 ended")
        if(complete) job.complete()
    }
    // if the complete flag is false this function will never end because the
    // job doesn't go to complete state that is required by join() to proceed
    job.join()
    println("Parent ended")
}