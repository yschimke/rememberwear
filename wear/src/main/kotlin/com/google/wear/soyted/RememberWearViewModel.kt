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

package com.google.wear.soyted;

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.soyted.db.Note
import com.google.wear.soyted.db.RememberWearDao
import com.google.wear.soyted.db.Task
import com.google.wear.soyted.db.TaskAndTaskSeries
import com.google.wear.soyted.db.TaskSeries
import com.google.wear.soyted.login.AuthRepository
import com.google.wear.soyted.login.LoginFlow
import com.google.wear.soyted.util.Toaster
import com.google.wear.soyted.work.ExternalUpdates
import com.google.wear.soyted.work.ScheduledWork
import com.google.wear.soyted.work.TaskCreator
import com.google.wear.soyted.work.TaskEditor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RememberWearViewModel @Inject constructor(
    private val rememberWearDao: RememberWearDao,
    private val scheduledWork: ScheduledWork,
    private val taskCreator: TaskCreator,
    private val taskEditor: TaskEditor,
    private val externalUpdates: ExternalUpdates,
    val authRepository: AuthRepository,
    val toaster: Toaster,
    val loginFlow: LoginFlow
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

    // TODO update this and refresh flow
    val today = LocalDate.now()

    val inbox = rememberWearDao.getAllTaskAndTaskSeries().map {
        it.filter {
            it.isUrgentUncompleted(today.plusDays(1)) ||
                    it.isRecentCompleted(today) ||
                    it.isCompletedOn(today)
        }.sortedBy { it.task.completed }
    }
        .stateIn(
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

    fun taskAndTaskSeries(taskId: String): Flow<TaskAndTaskSeries?> {
        return rememberWearDao.getTaskAndTaskSeries(taskId)
    }

    fun taskSeries(taskSeriesId: String): Flow<TaskSeries?> =
        rememberWearDao.getTaskSeries(taskSeriesId)

    fun taskSeriesNotes(taskSeriesId: String): Flow<List<Note>> =
        rememberWearDao.getTaskNotes(taskSeriesId)

    fun taskSeriesTasks(taskSeriesId: String): Flow<List<Task>> =
        rememberWearDao.getTasks(taskSeriesId)

    fun uncomplete(taskSeries: TaskSeries, task: Task) {
        viewModelScope.launch {
            try {
                taskEditor.uncomplete(taskSeries, task)
                externalUpdates.forceUpdates()
            } catch (ioe: IOException) {
                toaster.makeToast("Unable to connect to server")
            }
        }
    }

    fun complete(taskSeries: TaskSeries, task: Task) {
        viewModelScope.launch {
            try {
                taskEditor.complete(taskSeries, task)
                externalUpdates.forceUpdates()
            } catch (ioe: IOException) {
                toaster.makeToast("Unable to connect to server")
            }
        }
    }

    fun createTask(spokenText: String) {
        viewModelScope.launch {
            try {
                taskCreator.create(spokenText)
                externalUpdates.forceUpdates()
            } catch (ioe: IOException) {
                toaster.makeToast("Unable to connect to server")
            }
        }
    }

    fun enterLoginToken(it: String) {
        viewModelScope.launch {
            loginFlow.enterToken(it)
            scheduledWork.refetchAllDataWork()
        }
    }

    fun startLoginFlow() {
        viewModelScope.launch {
            loginFlow.startLogin()
        }
    }
}
