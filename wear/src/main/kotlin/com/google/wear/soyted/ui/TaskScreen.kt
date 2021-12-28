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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.wear.soyted.RememberWearViewModel
import com.google.wear.soyted.util.relativeTime
import com.google.wear.soyted.util.scrollHandler
import java.time.LocalDate

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: RememberWearViewModel,
    taskId: String,
    navController: NavHostController? = null
) {
    val task = viewModel.taskAndTaskSeries(taskId).collectAsState(initial = null).value
    val notes = if (task?.taskSeries?.id != null) {
        viewModel.taskSeriesNotes(task.taskSeries.id).collectAsState(listOf()).value
    } else {
        listOf()
    }

    val today = remember {
        LocalDate.now()
    }

    val taskSeries = task?.taskSeries
    val todayTask = task?.task

    // Activate scrolling
    LocalView.current.requestFocus()

    val scrollState = rememberScalingLazyListState()

    ScalingLazyColumn(
        modifier = modifier.fillMaxSize().scrollHandler(scrollState),
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (taskSeries != null) {
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
                            viewModel.complete(todayTask)
                            navController?.popBackStack()
                        } else {
                            viewModel.uncomplete(todayTask)
                        }
                    }, label = {
                        Text(
                            text = todayTask.dueDate?.relativeTime(today) ?: "Completed",
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
