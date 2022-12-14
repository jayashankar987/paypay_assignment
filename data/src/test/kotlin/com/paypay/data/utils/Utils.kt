package com.paypay.data.utils

import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

fun immediateExecutorService(): ExecutorService {
    return object : AbstractExecutorService() {
        override fun shutdown() = Unit

        override fun shutdownNow(): List<Runnable>? = null

        override fun isShutdown(): Boolean = false

        override fun isTerminated(): Boolean = false

        @Throws(InterruptedException::class)
        override fun awaitTermination(l: Long, timeUnit: TimeUnit): Boolean = false

        override fun execute(runnable: Runnable) = runnable.run()
    }
}