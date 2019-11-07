package co.cy.kotin101.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@UseExperimental(InternalCoroutinesApi::class)
fun main() {
    GlobalScope.launch {
        // launch a new coroutine in background and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello,") // main thread continues while coroutine is delayed
    Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive


    // runBlocking prevents the main thread from finishing, useful for testing coroutines
    runBlocking {
        val job = GlobalScope.launch {
            // launch a new coroutine and keep a reference to its Job
            delay(1000L)
            println("Job!")
        }
        println("Main")
        job.join() // wait until child coroutine completes
    }

    // dot dash synchronously
//    GlobalScope.async {
//        repeat(100_000) {
//            // launch a lot of coroutines
//            launch {
//                delay(1000L)
//                print(".")
//                dash()
//            }
//        }
//    }
//
//    // dot dash async
//    GlobalScope.async {
//        repeat(100_000) {
//            // launch a lot of coroutines
//            launch {
//                delay(1000L)
//                print(".")
//                // this async is a child of the launch and launch will not end until it's child is complete
//                launch { dash() }
//            }
//        }
//    }


    // Still experimental hopefully released soon
    runBlocking {
        println("Calling foo...")
        val flow = flowFoo()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    // coroutines can be named for better logging/debugging
    runBlocking(CoroutineName("main")) {
        log("Started main coroutine")
        // run two background value computations
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            log("Computing v1")
            252
        }
        val v2 = async(CoroutineName("v2coroutine")) {
            delay(1000)
            log("Computing v2")
            6
        }
        log("The answer for v1 / v2 = ${v1.await() / v2.await()}")

        suspend fun foo(): List<Int> {
            delay(1000) // pretend we are doing something asynchronous here
            return listOf(1, 2, 3)
        }
    }

    // Channels, blocking queues waiting for data, finding first 10 prime numbers
    runBlocking {
        var cur = numbersFrom(2)
        for (i in 1..10) {
            val prime = cur.receive()
            println(prime)
            cur = filter(cur, prime)
        }
        coroutineContext.cancelChildren() // cancel all children to let main finish
    }

    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) { // cancellable computation loop
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // delay a bit
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
        println("main: Now I can quit.")
    }
}

fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) send(x++) // infinite stream of integers from start
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numbers) if (x % prime != 0) send(x)
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun flowFoo(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun dash() {
    print("-")
}