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

// Job cancel memo
// When cancel is called:
// Cancels at first suspension point (delay, yield ...)
// Jobs children's will also be cancelled
// Job that is cancelled cannot be used as a parent for new jobs anymore
suspend fun testJobCancelingJob1(complete: Boolean = false) = coroutineScope {
    val job1 = launch {
        println("Job1 started")
        launch {
            println("Job1 child process started")
            delay(3000)
            println("Job1 child process stopped") // Never printed because of cancellation
        }
        delay(500)
        println("Job1 running ...")
        delay(500) // Cancel kicks in at this step
        println("Job1 ended") // Never printed because of cancellation
    }
    delay(500)
    job1.cancel()
    job1.join() // After canceling join should be called so that there wouldn't be race conditions
    delay(500)
    println("Parent process ended") // this will be printed still
}

suspend fun testJobCancelingJob2(complete: Boolean = false) = coroutineScope {
    val job1 = launch {
        println("Job1 started")
        launch {
            println("Job1 child process 1 started")
            delay(500)
            // Exception that inherit from cancellation exception will cancel the child coroutine -> parent coroutine -> and parents other childs if exists
            throw object : CancellationException("Canceling") {}
            delay(500)
            println("Job1 child process stopped")
        }
        launch {
            println("Job1 child process 2 started")
            delay(1000)
            println("Job1 child process 2 stopped") // Never printed because parent cancels this
        }

        delay(2000) // Never printed
        println("Job1 running ...") // Never printed
        delay(2000) // Never printed
        println("Job1 ended") // Never printed
    }
    delay(500)
    job1.cancel()
    job1.join()
    delay(500)
    println("Parent process ended") // this will be printed still
}
