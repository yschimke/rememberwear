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

package com.google.wear.soyted.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.wear.soyted.api.RememberTheMilkService
import com.google.wear.soyted.db.RememberWearDao
import com.google.wear.soyted.db.RememberWearDatabase
import com.google.wear.soyted.login.AuthRepository
import com.google.wear.soyted.util.createNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logcat.LogPriority
import logcat.logcat
import java.time.Instant
import java.time.temporal.ChronoUnit

@HiltWorker
class DataRefreshWorker
@AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val rememberWearDatabase: RememberWearDatabase,
    val rememberWearDao: RememberWearDao,
    val rememberTheMilkService: RememberTheMilkService,
    val externalUpdates: ExternalUpdates,
    val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        if (!authRepository.isLoggedIn.value) {
            return Result.failure()
        }

        refreshDatabase(rememberWearDatabase, rememberWearDao, rememberTheMilkService)

        externalUpdates.forceUpdates()

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(
                applicationContext, id,
                "Refreshing Tasks"
            )
        )
    }

    suspend fun refreshDatabase(
        rememberWearDatabase: RememberWearDatabase,
        rememberWearDao: RememberWearDao,
        rememberTheMilkService: RememberTheMilkService
    ) = withContext(Dispatchers.Default) {
        writeUpdates(rememberTheMilkService, rememberWearDao)

        val tags = rememberTheMilkService.tags()

        val now = Instant.now()
        val cutoff = now.minus(3, ChronoUnit.DAYS)

        val todosResponse = rememberTheMilkService.tasks("tag:wear")

        rememberWearDatabase.withTransaction {
            rememberWearDao.deleteAllTaskSeries()

            todosResponse.tasks?.list?.forEach { list ->
                list.taskseries?.forEach { taskSeries ->
                    val relevant =
                        taskSeries.task?.filter { it.completed == null || it.completed > cutoff }
                            .orEmpty()

                    val taskSeries1 = taskSeries.toDBTaskSeries(list.id)
                    rememberWearDao.upsertTaskSeries(taskSeries1)

                    if (relevant.isNotEmpty()) {
                        taskSeries.notes?.forEach { note ->
                            rememberWearDao.upsertNote(note.toDBNote(taskSeriesId = taskSeries.id))
                        }

                        relevant.forEach { task ->
                            rememberWearDao.upsertTask(
                                task.toDBTask(taskSeries.id)
                            )
                        }
                    }
                }

                tags.tags?.forEach {
                    rememberWearDao.upsertTag(it.toDBTag())
                }
            }
        }
    }

    private suspend fun writeUpdates(
        rememberTheMilkService: RememberTheMilkService,
        rememberWearDao: RememberWearDao
    ) {
        val todosResponse = rememberTheMilkService.tasks("tag:wear")

        val toUpdate = rememberWearDao.editedTasks()

        logcat(LogPriority.DEBUG) { "toUpdate $toUpdate" }

        val timeline = rememberTheMilkService.timeline().timeline.timeline
        toUpdate.forEach { dbTask ->
            val taskList =
                todosResponse.tasks?.list?.find { it.taskseries?.find { it.id == dbTask.taskSeriesId } != null }

            if (taskList == null) {
                logcat(LogPriority.WARN) { "Unable to update task $dbTask" }
            } else {
                if (dbTask.completed != null) {
                    rememberTheMilkService.complete(
                        timeline,
                        taskList.id,
                        dbTask.taskSeriesId,
                        dbTask.id
                    )
                } else {
                    rememberTheMilkService.uncomplete(
                        timeline,
                        taskList.id,
                        dbTask.taskSeriesId,
                        dbTask.id
                    )
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 101
    }
}
