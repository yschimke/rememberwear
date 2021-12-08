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

package com.google.wear.rememberwear.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.wear.rememberwear.db.TaskAndTaskSeries
import com.google.wear.rememberwear.util.relativeTime

@Composable
fun TaskSeriesChip(
    modifier: Modifier = Modifier,
    task: TaskAndTaskSeries,
    onClick: () -> Unit,
) {
    val due = task.task.dueDate

    Chip(
        modifier = modifier,
        onClick = onClick,
        colors = if (task.task.completed == null)
            ChipDefaults.primaryChipColors()
        else ChipDefaults.secondaryChipColors(),
        label = {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = task.taskSeries.name, maxLines = 2,
                    color = MaterialTheme.colors.onSurfaceVariant2,
                    style = MaterialTheme.typography.title3,
                )

                if (due != null) {
                    Text(
                        modifier = Modifier.sizeIn(minWidth = 32.dp, maxWidth = 48.dp),
                        text = due.relativeTime(),
                        color = MaterialTheme.colors.secondaryVariant,
                        maxLines = 2,
                        style = MaterialTheme.typography.caption1
                    )
                }
            }
        },
    )
}
