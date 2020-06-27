package com.maryam.sample.util

import kotlinx.coroutines.asCoroutineDispatcher

import java.util.concurrent.Executor

class InstantAppExecutors : AppExecutors(instantDispatcher, instantDispatcher, instantDispatcher) {
    companion object {
        private val instant = Executor { it.run() }
        private val instantDispatcher = instant.asCoroutineDispatcher()
    }
}