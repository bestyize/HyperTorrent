package com.xunlei.util

import java.util.concurrent.Executors


object TaskManager {

    private val EXECUTOR = Executors.newCachedThreadPool()

    fun submit(runnable: Runnable) {
        EXECUTOR.submit(runnable)
    }

    fun execute(runnable: Runnable) {
        EXECUTOR.execute(runnable)
    }
}