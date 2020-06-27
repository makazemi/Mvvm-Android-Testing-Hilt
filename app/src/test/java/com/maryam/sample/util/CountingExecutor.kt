package com.maryam.sample.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class Snapshot(
    private val executor: CountingExecutor,
    val actionCount: Int
) {
    fun isStillValid(): Boolean {
        return executor.wasIdleSince(this)
    }
}

/**
 * Simple [Executor] that uses a real thread but also counts # of active runnables.
 */
class CountingExecutor : Executor {
    // # of state changes. This allows ensuring that it was idle between two calls, mainly to
    // know whether anything new was scheduled after we've synced the controlled executors.
    private val actionCount = AtomicInteger()

    /**
     * Returns true if this Executor didn't have any change (enqueued tasks or finished tasks)
     * between now and the provided [snapshot] which is received from [createSnapshot].
     */
    fun wasIdleSince(snapshot: Snapshot): Boolean {
        val count = actionCount.get()
        return count % 2 == 0 && snapshot.actionCount == count
    }

    private val delegate by lazy {
        Executors.newSingleThreadExecutor()
    }

    fun createSnapshot() = Snapshot(this, actionCount.get())

    override fun execute(command: Runnable) {
        actionCount.incrementAndGet()
        delegate.submit {
            try {
                command.run()
            } finally {
                actionCount.incrementAndGet()
            }
        }
    }
}