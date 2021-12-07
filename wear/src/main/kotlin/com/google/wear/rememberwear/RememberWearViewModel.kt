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

package com.google.wear.rememberwear;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.rememberwear.db.Note
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.Task
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.db.TaskSeriesAndTasks
import com.google.wear.rememberwear.work.ScheduledWork
import com.google.wear.rememberwear.work.TaskEditor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class RememberWearViewModel @Inject constructor(
    private val rememberWearDao: RememberWearDao,
    private val scheduledWork: ScheduledWork,
    private val taskEditor: TaskEditor,
) : ViewModel() {
    val isRefreshing = MutableStateFlow(false)

    fun refetchAllData() {
        viewModelScope.launch {
            isRefreshing.value = true

            try {
                scheduledWork.refetchAllDataWork()
            } finally {
                isRefreshing.value = false
            }
        }
    }

    val inbox = rememberWearDao.getAllTaskSeriesAndTasks().stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    fun refetchIfStale() {
        viewModelScope.launch {
            val lastRefresh = rememberWearDao.getLastRefresh()

            val stale = lastRefresh == null ||
                    Duration.between(lastRefresh, Instant.now()) > Duration.ofMinutes(10)

            if (stale) {
                refetchAllData()
            }
        }
    }

    fun taskSeries(taskSeriesId: String): Flow<TaskSeries?> =
        rememberWearDao.getTaskSeries(taskSeriesId)

    fun taskSeriesNotes(taskSeriesId: String): Flow<List<Note>> =
        rememberWearDao.getTaskNotes(taskSeriesId)

    fun taskSeriesTasks(taskSeriesId: String): Flow<List<Task>> =
        rememberWearDao.getTasks(taskSeriesId)

    fun uncomplete(taskSeries: TaskSeries, task: Task) {
        viewModelScope.launch {
            taskEditor.uncomplete(taskSeries, task)
        }
    }

    fun complete(taskSeries: TaskSeries, task: Task) {
        viewModelScope.launch {
            taskEditor.complete(taskSeries, task)
        }
    }
}
