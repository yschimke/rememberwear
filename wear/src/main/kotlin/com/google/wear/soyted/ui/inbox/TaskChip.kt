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

package com.google.wear.soyted.ui.inbox

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.SplitToggleChip
import androidx.wear.compose.material.SplitToggleChipColors
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChipDefaults
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.previews.SampleData
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.previews.WearPreviewDevices
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme
import com.google.wear.soyted.ui.util.relativeTime
import java.time.Instant
import java.time.LocalDate

@Composable
fun TaskChip(
    modifier: Modifier = Modifier,
    task: TaskAndTaskSeries,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
    today: LocalDate
) {
    val due = task.task.dueDate
    val yesterday = remember { today.minusDays(1) }

    SplitToggleChip(modifier = modifier.fillMaxWidth(),
        checked = task.isCompleted,
        onClick = onClick,
        onCheckedChange = { onToggle(it) },
        colors = ToggleChipDefaults.splitToggleChipColors(
            checkedToggleControlColor = MaterialTheme.colors.primary
        ),
        label = {
            Text(
                text = task.taskSeries.name,
            )
        },
        secondaryLabel = if (due != null) {
            {
                Text(
                    text = due.relativeTime(today), color = when {
                        task.isCompleted -> Color.Gray
                        task.isUrgentUncompleted(yesterday) -> Color.Red
                        else -> Color.Unspecified
                    }
                )
            }
        } else null,
        toggleControl = {
            Icon(
                imageVector = ToggleChipDefaults.checkboxIcon(checked = task.isCompleted),
                contentDescription = if (task.isCompleted) "On" else "Off",
            )
        })
}


@WearPreviewDevices
@Composable
fun TaskChipPreview(@PreviewParameter(provider = TaskAndSeriesProvider::class) taskParam: TaskAndTaskSeries) {
    var task by remember { mutableStateOf(taskParam) }

    RememberTheMilkTheme {
        TaskChip(task = task, onClick = {}, onToggle = {
            val currentTask = task.task
            task = task.copy(task = currentTask.copy(completed = if (it) Instant.now() else null))
        }, today = SampleData.localDateTime.toLocalDate()
        )
    }
}
