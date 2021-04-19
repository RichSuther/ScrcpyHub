package model.repository

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProcessRepository {
    private val processList: MutableMap<String, Process> = mutableMapOf()

    fun insert(key: String, process: Process, onDestroy: (suspend () -> Unit)? = null) {
        processList[key]?.destroy()
        processList[key] = process

        // FIXME impl ProcessRepository coroutines scope
        MainScope().launch {
            process.waitForRunning(1000)
            process.monitor(1000) { onDestroy?.invoke() }
        }
    }

    fun delete(key: String) {
        processList[key]?.destroy()
        processList.remove(key)
    }

    fun deleteAll() {
        processList.values.forEach { it.destroy() }
        processList.clear()
    }

    private suspend fun Process.waitForRunning(interval: Long) {
        while (!this.isAlive) {
            delay(interval)
        }
    }

    private suspend fun Process.monitor(interval: Long, onDestroy: suspend () -> Unit) {
        while (this.isAlive) {
            delay(interval)
        }

        onDestroy.invoke()
    }
}