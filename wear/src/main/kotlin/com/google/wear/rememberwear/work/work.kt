package com.google.wear.rememberwear.work

import android.content.ComponentName
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.multiprocess.RemoteListenableWorker
import androidx.work.multiprocess.RemoteWorkerService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduledWork @Inject constructor(
    val workManager: WorkManager
) {
    // TODO move into class with DI injection of WorkManager
    suspend fun refetchAllDataWork() {
        val componentName =
            ComponentName("com.google.wear.rememberwear", RemoteWorkerService::class.java.name)

        val workRequest = OneTimeWorkRequestBuilder<DataRefreshWorker>()
            .setInputData(
                Data.Builder()
                    .putString(
                        RemoteListenableWorker.ARGUMENT_PACKAGE_NAME,
                        componentName.packageName
                    )
                    .putString(RemoteListenableWorker.ARGUMENT_CLASS_NAME, componentName.className)
                    .build()
            )
            .build()

        val result = workManager.enqueue(workRequest)

        result.result.await()
    }

    fun createPeriodicWorkRequest() {
        val componentName =
            ComponentName("com.google.wear.rememberwear", RemoteWorkerService::class.java.name)

        val refreshWorker = PeriodicWorkRequestBuilder<DataRefreshWorker>(
            15, TimeUnit.MINUTES, 10, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInitialDelay(15, TimeUnit.MINUTES)
            .addTag("locationRefresh")
            .setInputData(
                Data.Builder()
                    .putString(
                        RemoteListenableWorker.ARGUMENT_PACKAGE_NAME,
                        componentName.packageName
                    )
                    .putString(RemoteListenableWorker.ARGUMENT_CLASS_NAME, componentName.className)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodicLocationRefresh",
            ExistingPeriodicWorkPolicy.REPLACE,
            refreshWorker
        )
    }
}