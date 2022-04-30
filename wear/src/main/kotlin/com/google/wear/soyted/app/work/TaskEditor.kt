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
import com.google.wear.soyted.app.db.Task
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskEditor @Inject constructor(
    val service: RememberTheMilkService,
    val scheduledWork: ScheduledWork,
    val dao: RememberWearDao
) {
    suspend fun uncomplete(task: Task) {
        dao.upsertTask(task.copy(completed = null, edited = true))
    }

    suspend fun complete(task: Task) {
        dao.upsertTask(task.copy(completed = Instant.now(), edited = true))
    }
}
