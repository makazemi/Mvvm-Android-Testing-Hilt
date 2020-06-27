package com.maryam.sample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule

@ExperimentalCoroutinesApi
open class CoroutineTestBase {
    @JvmField
    @Rule
    val testExecutors = TestCoroutineAppExecutors()

    fun <T> LiveData<T>.addObserver(): CollectingObserver<T> {
        return testExecutors.runOnMain {
            val observer = CollectingObserver(this)
            observeForever(observer)
            observer
        }
    }

    fun advanceUntilIdle() = testExecutors.advanceUntilIdle()

    fun advanceTimeBy(time: Long) = testExecutors.advanceTimeBy(time)

    inner class CollectingObserver<T>(
        private val liveData: LiveData<T>
    ) : Observer<T> {
        private var items = mutableListOf<T>()
        override fun onChanged(t: T) {
            items.add(t)
        }

        fun assertItems(vararg expected: T) {
            MatcherAssert.assertThat(items, CoreMatchers.`is`(expected.asList()))
        }

        fun unsubscribe() = testExecutors.runOnMain {
            liveData.removeObserver(this)
        }

        fun reset() = testExecutors.runOnMain {
            items.clear()
        }
    }
}