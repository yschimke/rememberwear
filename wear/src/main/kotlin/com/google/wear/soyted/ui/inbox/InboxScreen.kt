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

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.previews.SampleData
import com.google.wear.soyted.previews.WearPreviewDevices
import com.google.wear.soyted.ui.input.VoicePrompt
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme
import com.google.wear.soyted.ui.util.ReportFullyDrawn
import com.google.wear.soyted.ui.util.rememberStateWithLifecycle
import java.time.LocalDate

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    columnState: ScalingLazyColumnState,
    viewModel: InboxViewModel = hiltViewModel(),
    navController: NavController,
    addTaskVoicePrompt: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {
    val state by rememberStateWithLifecycle(viewModel.state)
    val tasks = state.tasks
    val isRefreshing = state.isRefreshing
    val today = remember { LocalDate.now() }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refetchAllData() },
        refreshTriggerDistance = 16.dp
    ) {
        InboxScreen(
            modifier = modifier,
            columnState = columnState,
            tasks = tasks,
            onClick = {
                navController.navigateToTask(it.task.id)
            },
            onToggle = { task, completed ->
               viewModel.complete(task.task, completed)
            },
            voicePromptQuery = {
                addTaskVoicePrompt.launch(VoicePrompt.voicePromptIntent)
            },
            loginAction = {
                navController.navigateToLoginDialog()
            },
            isLoggedIn = state.isLoggedIn,
            today = today
        )
    }
}

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    columnState: ScalingLazyColumnState,
    tasks: List<TaskAndTaskSeries>?,
    today: LocalDate,
    onClick: (TaskAndTaskSeries) -> Unit,
    onToggle: (TaskAndTaskSeries, Boolean) -> Unit,
    voicePromptQuery: () -> Unit,
    loginAction: () -> Unit,
    isLoggedIn: Boolean?,
) {
    ScalingLazyColumn(
        modifier = modifier
            .semantics { contentDescription = "Inbox List" },
        columnState = columnState,
    ) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(1f),
                text = "Task List",
                style = MaterialTheme.typography.title1,
                textAlign = TextAlign.Center
            )
        }
        if (isLoggedIn == false) {
            item {
                Button(onClick = loginAction) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Login")
                }
            }
        }
        if (tasks != null) {
            items(tasks.size) { i ->
                val task = tasks[i]
                TaskChip(
                    task = task,
                    onClick = {
                        onClick(task)
                    },
                    onToggle = { completed ->
                        onToggle(task, completed)
                    },
                    today = today
                )
            }
        }
        if (isLoggedIn == true) {
            item {
                Button(onClick = voicePromptQuery) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Task")
                }
            }
        }
    }

    if (tasks != null) {
        ReportFullyDrawn()
    }
}

@WearPreviewDevices
@Composable
fun CirclesListPreview() {
    RememberTheMilkTheme {
        InboxScreen(
            tasks = SampleData.taskAndTaskSeries,
            onClick = {},
            voicePromptQuery = {},
            loginAction = {},
            isLoggedIn = true,
            onToggle = { _, _ ->
            },
            today = SampleData.localDateTime.toLocalDate(),
            columnState = ScalingLazyColumnDefaults.belowTimeText().create()
        )
    }
}

@WearPreviewDevices
@Composable
fun CirclesListNotLoggedInPreview() {
    RememberTheMilkTheme {
        InboxScreen(
            tasks = listOf(),
            onClick = {},
            voicePromptQuery = {},
            loginAction = {},
            isLoggedIn = false,
            onToggle = { _, _ -> },
            today = SampleData.localDateTime.toLocalDate(),
            columnState = ScalingLazyColumnDefaults.belowTimeText().create()
        )
    }
}
