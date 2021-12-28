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

package com.google.wear.soyted.api.model.tasks

import com.google.wear.soyted.db.Task
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import java.time.Instant
import java.time.ZoneId

@Xml(name = "task")
data class Task(
    @Attribute
    val id: String,

    @Attribute
    val due: Instant?,

    @Attribute
    val has_due_time: Int,

    @Attribute
    val added: Instant,

    @Attribute
    val completed: Instant?,

    @Attribute
    val deleted: Instant?,

    @Attribute
    val priority: String,

    @Attribute
    val postponed: String,

    @Attribute
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
