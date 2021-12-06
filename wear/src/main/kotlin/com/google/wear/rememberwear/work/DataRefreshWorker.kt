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

package com.google.wear.rememberwear.work;

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import com.google.wear.rememberwear.api.RememberTheMilkService
import com.google.wear.rememberwear.complication.RememberWearComplicationProviderService
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.RememberWearDatabase
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.tile.RememberWearTileProviderService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

@HiltWorker
class DataRefreshWorker
@AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val rememberWearDatabase: RememberWearDatabase,
    val rememberWearDao: RememberWearDao,
    val rememberTheMilkService: RememberTheMilkService
) : RemoteCoroutineWorker(appContext, workerParams) {
    override suspend fun doRemoteWork(): Result {
        refreshDatabase(rememberWearDatabase, rememberWearDao, rememberTheMilkService)

        RememberWearTileProviderService.forceTileUpdate(applicationContext)
        RememberWearComplicationProviderService.forceComplicationUpdate(applicationContext)

        return Result.success()
    }

    suspend fun refreshDatabase(
        rememberWearDatabase: RememberWearDatabase,
        rememberWearDao: RememberWearDao,
        rememberTheMilkService: RememberTheMilkService
    ) = withContext(Dispatchers.Default) {
        val todosResponse = rememberTheMilkService.tasks("tag:wear")
        val tags = rememberTheMilkService.tags()

        val now = Instant.now()
        val cutoff = now.minus(3, ChronoUnit.DAYS)

        rememberWearDatabase.withTransaction {
            rememberWearDao.deleteAllTaskSeries()

            todosResponse.tasks?.taskSeries?.forEach { taskSeries ->
                val relevant =
                    taskSeries.task?.filter { it.completed == null || it.completed > cutoff }.orEmpty()

                val incomplete = relevant.filter { it.completed == null }
                val due = incomplete.mapNotNull { it.due }.minOrNull()

                val repeating = taskSeries.rrule != null
                rememberWearDao.upsertTaskSeries(TaskSeries(taskSeries.id, taskSeries.name, due, taskSeries.created, taskSeries.modified, repeating))

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
