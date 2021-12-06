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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import com.google.wear.rememberwear.RememberWearViewModel
import com.google.wear.rememberwear.util.relativeTime
import java.time.LocalDate

@Composable
fun TaskSeriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RememberWearViewModel,
    taskSeriesId: String
) {
    val taskSeries = viewModel.taskSeries(taskSeriesId).collectAsState(initial = null).value
    val notes = viewModel.taskSeriesNotes(taskSeriesId).collectAsState(listOf()).value
    val tasks = viewModel.taskSeriesTasks(taskSeriesId).collectAsState(listOf()).value

    val today = remember {
        LocalDate.now()
    }

    ScalingLazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (taskSeries != null) {
            val todayTask = if (taskSeries.isRepeating) {
                tasks.find { it.dueDate == today }
            } else {
                tasks.firstOrNull()
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = taskSeries.name,
                    style = MaterialTheme.typography.title1,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }

            if (todayTask != null) {
                item {
                    ToggleChip(checked = todayTask.completed != null, onCheckedChange = {
                        if (it) {
                            viewModel.complete(taskSeries, todayTask)
                        } else {
                            viewModel.uncomplete(taskSeries, todayTask)
                        }
                    }, label = {
                        Text(
                            text = todayTask.due?.relativeTime() ?: "Completed",
                        )
                    })
                }
            }
            items(notes.size) {
                Text(notes[it].body)
            }
        }
    }
}
