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

import com.google.wear.rememberwear.api.RememberTheMilkService
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.Task
import com.google.wear.rememberwear.db.TaskSeries
import java.time.Instant
import javax.inject.Inject

class TaskEditor @Inject constructor(
    val service: RememberTheMilkService,
    val dao: RememberWearDao
) {
    suspend fun uncomplete(taskSeries: TaskSeries, task: Task) {
        val timeline = service.timeline().timeline.timeline

        service.uncomplete(timeline, taskSeries.listId, taskSeries.id, task.id)
        dao.upsertTask(task.copy(completed = null))
    }

    suspend fun complete(taskSeries: TaskSeries, task: Task) {
        val timeline = service.timeline().timeline.timeline
        service.complete(timeline, taskSeries.listId, taskSeries.id, task.id)

        dao.upsertTask(task.copy(completed = Instant.now()))
    }
}