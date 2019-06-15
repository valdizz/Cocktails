package com.valdizz.cocktails

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.jetbrains.spek.api.dsl.Spec

fun Spec.emulateInstantTaskExecutorRule() {

    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(SingleThreadTaskExecutor())
    }

    afterEachTest {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

class SingleThreadTaskExecutor() : TaskExecutor() {
    override fun executeOnDiskIO(runnable: Runnable) {
        runnable.run()
    }

    override fun isMainThread(): Boolean {
        return true
    }

    override fun postToMainThread(runnable: Runnable) {
        runnable.run()
    }
}