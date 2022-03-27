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

package com.google.wear.soyted.ui.task

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import com.google.wear.soyted.app.db.Note
import com.google.wear.soyted.app.db.Task
import com.google.wear.soyted.app.db.TaskSeries
import com.google.wear.soyted.horologist.scrollableColumn
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.util.relativeTime
import com.google.wear.soyted.ui.util.rememberStateWithLifecycle
import java.time.LocalDate

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    navController: NavController,
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    TaskScreen(
        modifier = modifier,
        focusRequester = focusRequester,
        scrollState = scrollState,
        taskSeries = state.taskSeries,
        task = state.todayTask,
        today = state.today,
        notes = state.notes,
        onComplete = {
            viewModel.complete(it)
            navController.popBackStack()
        },
        onUncomplete = {
            viewModel.uncomplete(it)
        }
    )
}

@Composable
public fun TaskScreen(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    scrollState: ScalingLazyListState,
    taskSeries: TaskSeries?,
    task: Task?,
    today: LocalDate,
    notes: List<Note>?,
    onComplete: (Task) -> Unit,
    onUncomplete: (Task) -> Unit,
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState),
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = taskSeries?.name.orEmpty(),
                style = MaterialTheme.typography.title1,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }

        if (task != null) {
            item {
                ToggleChip(checked = task.completed != null, onCheckedChange = {
                    if (it) {
                        onComplete(task)
                    } else {
                        onUncomplete(task)
                    }
                }, label = {
                    Text(
                        text = task.dueDate?.relativeTime(today) ?: "Completed",
                    )
                })
            }
        }
        if (notes != null) {
            items(notes.size) {
                Text(notes[it].body)
            }
        }
    }
}
