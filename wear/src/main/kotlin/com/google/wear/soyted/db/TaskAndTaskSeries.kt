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

package com.google.wear.soyted.db

import androidx.room.Embedded
import androidx.room.Relation
import java.time.LocalDate
import java.time.ZoneId

data class TaskAndTaskSeries(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskSeriesId",
        entityColumn = "id"
    )
    val taskSeries: TaskSeries
) {
    val isCompleted: Boolean
        get() = task.completed != null

    fun isRecentCompleted(today: LocalDate?) = task.completed != null && task.dueDate == today

    fun isUrgentUncompleted(lastDay: LocalDate?) =
        task.completed == null && (task.dueDate == null || task.dueDate <= lastDay)

    fun isCompletedOn(today: LocalDate?) =
        task.completed?.atZone(ZoneId.systemDefault())?.toLocalDate() == today
}