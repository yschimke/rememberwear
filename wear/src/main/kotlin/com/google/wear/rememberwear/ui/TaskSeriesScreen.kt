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

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.ToggleChip
import com.google.wear.rememberwear.RememberWearViewModel
import com.google.wear.rememberwear.util.relativeTime

@Composable
fun TaskSeriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RememberWearViewModel,
    taskSeriesId: String
) {
    val taskSeries by viewModel.taskSeries(taskSeriesId).collectAsState(initial = null)
    val notes by viewModel.taskSeriesNotes(taskSeriesId).collectAsState(listOf())
    val tasks by viewModel.taskSeriesTasks(taskSeriesId).collectAsState(listOf())

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        val taskSeries = taskSeries
        if (taskSeries != null) {
            TitleCard(
                modifier = modifier,
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        text = taskSeries.name,
                        maxLines = 2
                    )
                },
                onClick = {}
            ) {
                Column {
                    if (taskSeries.due != null) {
                        val completed = false

                        ToggleChip(checked = completed, onCheckedChange = {
                            if (completed) {
                                viewModel.uncomplete(taskSeries)
                            } else {
                                viewModel.complete(taskSeries)
                            }
                        }, label = {
                            Text(
                                text = taskSeries.due.relativeTime(),
                            )
                        })
                    }
                }

                notes.forEach {
                    Text(it.body)
                }

                if (taskSeries.isRepeating) {
                    tasks.forEach {

                    }
                }
            }
        }
    }
}
