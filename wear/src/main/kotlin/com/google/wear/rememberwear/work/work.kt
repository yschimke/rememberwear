/*
 * Copyright 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.rememberwear.work

import android.content.ComponentName
import androidx.work.*
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
            30, TimeUnit.MINUTES, 10, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInitialDelay(15, TimeUnit.MINUTES)
            .addTag("taskRefresh")
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