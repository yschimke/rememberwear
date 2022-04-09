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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.soyted.app.db.RememberWearDao
import com.google.wear.soyted.app.db.Task
import com.google.wear.soyted.app.work.ScheduledWork
import com.google.wear.soyted.app.work.TaskEditor
import com.google.wear.soyted.ui.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val rememberWearDao: RememberWearDao,
    private val scheduledWork: ScheduledWork,
    private val taskEditor: TaskEditor,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val isRefreshingState = MutableStateFlow(false)

    val today = LocalDate.now()

    val state: StateFlow<InboxScreenState> = combine(
        isRefreshingState,
        allTasks(),
        authRepository.isLoggedIn
    ) { isRefreshing, allTaskAndTaskSeries, isLoggedIn ->
        InboxScreenState(
            tasks = allTaskAndTaskSeries,
            isRefreshing = isRefreshing,
            isLoggedIn = isLoggedIn
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InboxScreenState.Empty,
        )

    fun refetchAllData() {
        viewModelScope.launch {
            isRefreshingState.value = true

            try {
                scheduledWork.refetchAllDataWork()
            } finally {
                isRefreshingState.value = false
            }
        }
    }

    private fun allTasks() = rememberWearDao.getAllTaskAndTaskSeries().map { tasks ->
        tasks.filter { task ->
            task.isUrgentUncompleted(today.plusDays(1)) ||
                    task.isRecentCompleted(today) ||
                    task.isCompletedOn(today)
        }.sortedBy { it.task.completed }
    }

    fun complete(task: Task, completed: Boolean) {
        viewModelScope.launch {
            if (completed) {
                taskEditor.complete(task)
            } else {
                taskEditor.uncomplete(task)
            }
        }
    }
}
