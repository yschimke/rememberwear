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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.wear.soyted.RememberWearViewModel
import com.google.wear.soyted.db.TaskAndTaskSeries
import com.google.wear.soyted.util.RotaryEventState

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(),
    onClick: (TaskAndTaskSeries) -> Unit = {},
    viewModel: RememberWearViewModel,
    voicePromptQuery: () -> Unit,
    loginAction: () -> Unit,
) {
    val config = LocalConfiguration.current

    SideEffect {
        println("" + config.screenHeightDp + " " + config.screenWidthDp)
    }

    val tasks = viewModel.inbox.collectAsState(initial = listOf()).value

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    RotaryEventState(scrollState)

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
            onClick = onClick,
            voicePromptQuery = voicePromptQuery,
            loginAction = loginAction,
            isLoggedIn = isLoggedIn
        )
    }
}

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(),
    tasks: List<TaskAndTaskSeries>,
    onClick: (TaskAndTaskSeries) -> Unit,
    voicePromptQuery: () -> Unit,
    loginAction: () -> Unit,
    isLoggedIn: Boolean
) {
    val paddingHeight = if (LocalConfiguration.current.isScreenRound) 24.dp else 8.dp

    ScalingLazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = paddingHeight,
        ),
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
        items(tasks.size) {
            TaskChip(task = tasks[it], onClick = { onClick(tasks[it]) })
        }
        if (isLoggedIn) {
            item {
                Button(onClick = voicePromptQuery) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Task")
                }
            }
        }
    }
}
