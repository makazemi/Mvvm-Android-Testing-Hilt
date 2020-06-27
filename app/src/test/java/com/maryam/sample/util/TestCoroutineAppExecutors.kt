package com.maryam.sample.util


import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestCoroutineAppExecutors : TestWatcher() {
    val mainDispatcher = TestCoroutineDispatcher()
    val defaultDispatcher = TestCoroutineDispatcher()
    val ioDispatcher = TestCoroutineDispatcher()
    private val roomTransactionExecutor = CountingExecutor()
    private val roomQueryExecutor = CountingExecutor()

    private val dispatchers = listOf(mainDispatcher, defaultDispatcher, ioDispatcher)
    private val executors = listOf(roomTransactionExecutor, roomQueryExecutor)

    val appExecutors = AppExecutors(
        mainThread = mainDispatcher,
        default = defaultDispatcher,
        io = ioDispatcher
    )

    fun setupRoom(builder: RoomDatabase.Builder<*>) {
        builder.setQueryExecutor(roomQueryExecutor)
        builder.setTransactionExecutor(roomTransactionExecutor)
    }

    fun advanceTimeBy(time: Long) {
        dispatchers.forEach {
            it.advanceTimeBy(time)
        }
        advanceUntilIdle()
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(mainDispatcher.coroutineDispatcher())
    }

    private fun CoroutineContext.coroutineDispatcher() =
        this[ContinuationInterceptor] as CoroutineDispatcher

    override fun finished(description: Description?) {
        super.finished(description)
        advanceUntilIdle()
        Dispatchers.resetMain()
    }

    fun <T> runOnMain(block: () -> T): T {
        return runBlocking {
            val async = async(Dispatchers.Main) {
                block()
            }
            advanceUntilIdle()
            async.await()
        }
    }

    fun advanceUntilIdle() {
        do {
            // get current state signatures from Room's executors so that we
            // know if they execute anything in between
            val snapshots = executors.map {
                it.createSnapshot()
            }
            // trigger all controlled actions
            dispatchers.forEach {
                it.advanceUntilIdle()
            }
            // now check if all are idle + executors didn't do any work.
            val allIdle = dispatchers.all {
                it.isIdle()
            } && snapshots.all {
                it.isStillValid()
            }
        } while (!allIdle)
    }
}