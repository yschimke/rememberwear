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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.home.TaskChip
import com.google.wear.soyted.horologist.scrollableColumn
import com.google.wear.soyted.ui.input.VoicePrompt
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.util.ReportFullyDrawn
import com.google.wear.soyted.ui.util.rememberStateWithLifecycle

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState,
    viewModel: InboxViewModel = hiltViewModel(),
    navController: NavController,
    addTaskVoicePrompt: ManagedActivityResultLauncher<Intent, ActivityResult>,
    focusRequester: FocusRequester
) {
    val state by rememberStateWithLifecycle(viewModel.state)
    val tasks = state.tasks
    val isRefreshing = state.isRefreshing

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refetchAllData() },
        refreshTriggerDistance = 16.dp
    ) {
        val isLoggedIn by viewModel.authRepository.isLoggedIn.collectAsState()

        InboxScreen(
            modifier = modifier,
            scrollState = scrollState,
            tasks = tasks,
            onClick = {
                navController.navigateToTask(it.task.id)
            },
            voicePromptQuery = {
                addTaskVoicePrompt.launch(VoicePrompt.voicePromptIntent)
            },
            loginAction = {
                navController.navigateToLoginDialog()
            },
            isLoggedIn = isLoggedIn,
            focusRequester = focusRequester
        )
    }
}

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState,
    tasks: List<TaskAndTaskSeries>?,
    onClick: (TaskAndTaskSeries) -> Unit,
    voicePromptQuery: () -> Unit,
    loginAction: () -> Unit,
    isLoggedIn: Boolean,
    focusRequester: FocusRequester
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState)
            .semantics { contentDescription = "Inbox List" },
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(1f),
                text = "Task List",
                style = MaterialTheme.typography.title1,
                textAlign = TextAlign.Center
            )
        }
        item {

        }
        if (!isLoggedIn) {
            item {
                Button(onClick = loginAction) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Login")
                }
            }
        }
        if (tasks != null) {
            items(tasks.size) {
                TaskChip(task = tasks[it], onClick = {
                    onClick(tasks[it])
                })
            }
        }
        if (isLoggedIn) {
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
