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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.android.horologist.compose.navscaffold.scrollableColumn
import com.google.wear.soyted.app.db.Note
import com.google.wear.soyted.app.db.Task
import com.google.wear.soyted.app.db.TaskSeries
import com.google.wear.soyted.previews.SampleData
import com.google.wear.soyted.previews.WearPreviewDevices
import com.google.wear.soyted.previews.WearPreviewFontSizes
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme
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
        onToggle = { task, completed ->
            viewModel.complete(task, completed)
            navController.popBackStack()
        },
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
    onToggle: (Task, Boolean) -> Unit,
) {
    ScalingLazyColumn(
        modifier = modifier.scrollableColumn(focusRequester, scrollState),
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
                val completed = task.completed != null
                ToggleChip(
                    modifier = Modifier.fillMaxWidth(),
                    checked = completed,
                    onCheckedChange = {
                        onToggle(task, it)
                    },
                    label = {
                        Text(
                            text = task.dueDate?.relativeTime(today) ?: "Completed",
                        )
                    },
                    toggleControl = {
                        Icon(
                            imageVector = ToggleChipDefaults.switchIcon(checked = completed),
                            contentDescription = if (completed) "On" else "Off",
                        )
                    }
                )
            }
        } else {
            item {
                ToggleChip(
                    checked = false,
                    onCheckedChange = {
                    },
                    label = {},
                    toggleControl = {
                        Icon(
                            imageVector = ToggleChipDefaults.switchIcon(checked = false),
                            contentDescription = "Off",
                        )
                    }
                )
            }
        }
        if (notes != null) {
            items(notes.size) {
                Text(notes[it].body)
            }
        } else {
            item {
                Text("")
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontSizes
@Composable
fun TaskScreenPreview() {
    RememberTheMilkTheme {
        TaskScreen(
            focusRequester = remember { FocusRequester() },
            scrollState = rememberScalingLazyListState(),
            taskSeries = SampleData.taskSeries,
            task = SampleData.tasks.first(),
            today = SampleData.localDateTime.toLocalDate(),
            notes = null,
            onToggle = { _, _ -> }
        )
    }
}
