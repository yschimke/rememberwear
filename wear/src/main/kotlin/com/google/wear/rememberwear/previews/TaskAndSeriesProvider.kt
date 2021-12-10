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

package com.google.wear.rememberwear.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.wear.rememberwear.db.Task
import com.google.wear.rememberwear.db.TaskAndTaskSeries
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.previews.Previews.localDateTime
import com.google.wear.rememberwear.previews.Previews.timestamp
import java.time.Instant
import java.time.LocalDate

class TaskAndSeriesProvider : PreviewParameterProvider<TaskAndTaskSeries> {

    override val values: Sequence<TaskAndTaskSeries> = taskAndTaskSeries.asSequence()

    companion object {
        val taskSeries = TaskSeries("1", "all", "Take Meds", timestamp, timestamp, true)

        val taskYesterdayNotCompleted = task("1a", localDateTime.toLocalDate().minusDays(1), null)
        val taskYesterdayCompleted = task("1a", localDateTime.toLocalDate().minusDays(1), timestamp)
        val taskTodayNotCompleted = task("1a", localDateTime.toLocalDate(), null)
        val taskTodayCompleted = task("1a", localDateTime.toLocalDate(), timestamp)
        val taskTomorrowNotCompleted = task("1a", localDateTime.toLocalDate().plusDays(1), null)

        val tasks = listOf(
            taskYesterdayNotCompleted,
            taskYesterdayCompleted,
            taskTodayNotCompleted,
            taskTodayCompleted,
            taskTomorrowNotCompleted
        )

        val taskAndTaskSeries = tasks.map { TaskAndTaskSeries(it, taskSeries) }

        fun task(id: String, dueDate: LocalDate?, completed: Instant?): Task {
            return Task(
                id,
                "1",
                dueDate = dueDate,
                added = timestamp,
                completed = completed,
                deleted = null,
                priority = "N",
                postponed = "",
                estimate = ""
            )
        }
    }
}