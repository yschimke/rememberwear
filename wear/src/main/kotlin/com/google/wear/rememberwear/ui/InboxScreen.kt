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

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.wear.rememberwear.RememberWearViewModel
import com.google.wear.rememberwear.db.TaskAndTaskSeries
import com.google.wear.rememberwear.previews.RememberTheMilkThemePreview
import com.google.wear.rememberwear.previews.TaskAndSeriesProvider
import com.google.wear.rememberwear.util.RotaryEventState

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(),
    onClick: (TaskAndTaskSeries) -> Unit = {},
    viewModel: RememberWearViewModel
) {
    val tasks = viewModel.inbox.collectAsState(initial = listOf()).value

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    RotaryEventState(scrollState)

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refetchAllData() },
        refreshTriggerDistance = 16.dp
    ) {
        InboxScreen(modifier, scrollState, tasks, onClick)
    }
}

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(),
    tasks: List<TaskAndTaskSeries>,
    onClick: (TaskAndTaskSeries) -> Unit
) {
    val paddingHeight = if (LocalConfiguration.current.isScreenRound) 24.dp else 8.dp

    ScalingLazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = paddingHeight,
        ),
        state = scrollState
    ) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(1f),
                text = "Task List",
                style = MaterialTheme.typography.title1,
                textAlign = TextAlign.Center
            )
        }
        items(tasks.size) {
            TaskChip(task = tasks[it], onClick = { onClick(tasks[it]) })
        }
    }
}

@Preview(
    widthDp = 300,
    heightDp = 300,
    apiLevel = 26,
    uiMode = Configuration.UI_MODE_TYPE_WATCH
)
@Composable
fun CirclesListPreview() {
    RememberTheMilkThemePreview(round = true) {
        InboxScreen(tasks = TaskAndSeriesProvider.taskAndTaskSeries, onClick = {})
    }
}