package com.maryam.sample.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
@Singleton
open class AppExecutors(
    val io: CoroutineDispatcher,
    val default : CoroutineDispatcher,
    val mainThread: CoroutineDispatcher
) {
    @Inject
    constructor() : this(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main
    )

    // temporary fields during migration to coroutines
    private val diskExecutor = DispatcherExecutor(io)
    private val networkExecutor = DispatcherExecutor(io)
    private val mainExecutor = DispatcherExecutor(mainThread)

    @Deprecated("use dispatchers")
    fun diskIO(): Executor {
        return diskExecutor
    }

    @Deprecated("use dispatchers")
    fun networkIO(): Executor {
        return networkExecutor
    }

    @Deprecated("use dispatchers")
    fun mainThread(): Executor {
        return mainExecutor
    }
}

/**
 * Temporary class during migration from executors to Coroutines
 */
private class DispatcherExecutor(val dispatcher : CoroutineDispatcher) : Executor {
    override fun execute(command: java.lang.Runnable) {
        dispatcher.dispatch(EmptyCoroutineContext, command)
    }
}