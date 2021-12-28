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

import com.google.wear.soyted.api.RememberTheMilkService
import com.google.wear.soyted.db.RememberWearDao
import com.google.wear.soyted.db.Task
import com.google.wear.soyted.db.TaskSeries
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskEditor @Inject constructor(
    val service: RememberTheMilkService,
    val scheduledWork: ScheduledWork,
    val dao: RememberWearDao
) {
    suspend fun uncomplete(taskSeries: TaskSeries, task: Task) {
        dao.upsertTask(task.copy(completed = null, edited = true))

        scheduledWork.refetchAllDataWork()
    }

    suspend fun complete(taskSeries: TaskSeries, task: Task) {
        dao.upsertTask(task.copy(completed = Instant.now(), edited = true))

        scheduledWork.refetchAllDataWork()
    }
}