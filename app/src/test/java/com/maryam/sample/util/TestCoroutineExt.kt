package com.maryam.sample.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

// Utility class to observe TestCoroutineContext internals until the new TestCoroutineContext
// APIs are available
@ExperimentalCoroutinesApi
fun TestCoroutineDispatcher.isIdle(): Boolean {
    val queueField = this::class.java
        .getDeclaredField("queue")
    queueField.isAccessible = true
    val queue = queueField.get(this)
    val peekMethod = queue::class.java
        .getDeclaredMethod("peek")
    val nextTask = peekMethod.invoke(queue) ?: return true
    val timeField = nextTask::class.java.getDeclaredField("time")
    timeField.isAccessible = true
    val time = timeField.getLong(nextTask)
    return time > currentTime
}