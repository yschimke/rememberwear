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

package com.google.wear.soyted.app.api.model.tasks

import com.google.wear.soyted.app.api.model.util.InstantTypeConverter
import com.google.wear.soyted.app.db.Task
import kotlinx.serialization.SerialName
import java.time.Instant
import java.time.ZoneId

@kotlinx.serialization.Serializable
@SerialName("task")
data class Task(
    val id: String,

    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    val due: Instant?,

    val has_due_time: Int,

    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    val added: Instant?,

    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    val completed: Instant?,

    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    val deleted: Instant?,

    val priority: String,

    val postponed: String,

    val estimate: String,
) {
    fun toDBTask(taskSeriesId: String): Task = Task(
        id = this.id,
        taskSeriesId = taskSeriesId,
        dueDate = this.due?.atZone(ZoneId.systemDefault())?.toLocalDate(),
        added = this.added,
        completed = this.completed,
        deleted = this.deleted,
        edited = false,
        priority = this.priority,
        postponed = this.postponed,
        estimate = this.estimate
    )
}
