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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.SplitToggleChip
import androidx.wear.compose.material.Text
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.ui.util.relativeTime
import java.time.LocalDate

@Composable
fun TaskChip(
    modifier: Modifier = Modifier,
    task: TaskAndTaskSeries,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
) {
    val due = task.task.dueDate
    val today = LocalDate.now()
    val yesterday = remember { today.minusDays(1) }

    SplitToggleChip(
        modifier = modifier,
        checked = task.isCompleted,
        onClick = onClick,
        onCheckedChange = { onToggle(it) },
        label = {
            Text(
                text = task.taskSeries.name,
            )
        },
        secondaryLabel = ifNotNull(due) {
            Text(
                text = due.relativeTime(),
                color = when {
                    task.isCompleted -> Color.Gray
                    task.isUrgentUncompleted(yesterday) -> Color.Red
                    else -> Color.Unspecified
                }
            )
        }

    )
}

fun ifNotNull(item: Any?, function: @Composable () -> Unit): @Composable (() -> Unit)? =
    if (item != null) function else null
