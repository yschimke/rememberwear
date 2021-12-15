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

package com.google.wear.soyted.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ChipDefaults.chipColors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.wear.soyted.db.TaskAndTaskSeries
import com.google.wear.soyted.previews.RememberTheMilkThemePreview
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.util.relativeTime
import java.time.LocalDate

@Composable
fun TaskChip(
    modifier: Modifier = Modifier,
    task: TaskAndTaskSeries,
    onClick: () -> Unit,
) {
    val due = task.task.dueDate
    val today = LocalDate.now()
    val yesterday = remember { today.minusDays(1) }

    val chipColor =
        when {
            task.isCompleted -> ChipDefaults.secondaryChipColors()
            due != null && due > today -> chipColors(
                backgroundColor = MaterialTheme.colors.primaryVariant
            )
            else -> ChipDefaults.primaryChipColors()
        }
    Chip(
        modifier = modifier,
        onClick = onClick,
        colors = chipColor,
        label = {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = task.taskSeries.name, maxLines = 2,
                    color = MaterialTheme.colors.onSurfaceVariant2,
                    style = MaterialTheme.typography.title3,
                )

                if (due != null) {
                    val color = if (task.isCompleted) {
                        Color.Gray
                    } else if (task.isUrgentUncompleted(yesterday)) {
                        Color.Red
                    } else {
                        MaterialTheme.colors.secondaryVariant
                    }
                    Text(
                        modifier = Modifier.sizeIn(minWidth = 32.dp, maxWidth = 48.dp),
                        text = due.relativeTime(),
                        color = color,
                        maxLines = 2,
                        style = MaterialTheme.typography.caption1
                    )
                }
            }
        },
    )
}
