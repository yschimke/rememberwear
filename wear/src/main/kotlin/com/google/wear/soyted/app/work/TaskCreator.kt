/*
 * Copyright 2021-2022 Google Inc. All rights reserved.
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

package com.google.wear.soyted.app.work

import com.google.wear.soyted.app.api.RememberTheMilkService
import com.google.wear.soyted.app.db.RememberWearDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskCreator @Inject constructor(
    val service: RememberTheMilkService,
    val dao: RememberWearDao
) {
    suspend fun create(name: String) {
        val timeline = service.timeline().timeline.timeline
        val list = service.lists().lists.first().id
        val added = service.addTask(timeline, name, parse = "1", list_id = list)

        val taskSeries =
            added.list.taskseries?.first() ?: throw IllegalStateException("No taskseries")
        val task = taskSeries.task?.first() ?: throw IllegalStateException("No task")
        service.setTags(timeline, list, taskSeries.id, task.id, "wear")

        dao.upsertTaskSeries(taskSeries.toDBTaskSeries(list))
        dao.upsertTask(task.toDBTask(taskSeries.id))
    }
}