package com.hyvu.thuytrangmarket.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NetworkCoroutineScope : CoroutineScope {

    companion object {
        fun getInstance(): NetworkCoroutineScope {
            return NetworkCoroutineScopeHolder.instance
        }
    }

    object NetworkCoroutineScopeHolder {
        val instance = NetworkCoroutineScope()
    }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun launchBlocking(block: () -> Unit) {
        launch {
            block.invoke()
        }
    }

    fun launchSuspend(block: suspend () -> Unit) {
        launch {
            block.invoke()
        }
    }

    fun cancelScope() {
        job.cancel() // Cancels all coroutines in the scope
    }
}