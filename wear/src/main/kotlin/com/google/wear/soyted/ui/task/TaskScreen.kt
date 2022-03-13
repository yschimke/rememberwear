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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.util.relativeTime
import com.google.wear.soyted.ui.util.rememberStateWithLifecycle
import com.google.wear.soyted.ui.util.scrollHandler

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    navController: NavController,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(initialCenterItemIndex = 1)
) {
    val state by rememberStateWithLifecycle(viewModel.state)
    val task = state.todayTask
    val taskSeries = state.taskSeries
    val notes = state.notes ?: listOf()

    // Activate scrolling
    LocalView.current.requestFocus()

    if (taskSeries != null) {
        ScalingLazyColumn(
            modifier = modifier
                .fillMaxSize()
                .scrollHandler(scrollState),
            state = scrollState,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = taskSeries.name,
                    style = MaterialTheme.typography.title1,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }

            if (task != null) {
                item {
                    ToggleChip(checked = task.completed != null, onCheckedChange = {
                        if (it) {
                            viewModel.complete(task)
                            navController.popBackStack()
                        } else {
                            viewModel.uncomplete(task)
                        }
                    }, label = {
                        Text(
                            text = task.dueDate?.relativeTime(state.today) ?: "Completed",
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
