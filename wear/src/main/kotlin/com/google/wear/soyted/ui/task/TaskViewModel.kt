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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.soyted.app.db.RememberWearDao
import com.google.wear.soyted.app.db.Task
import com.google.wear.soyted.app.work.ExternalUpdates
import com.google.wear.soyted.app.work.TaskEditor
import com.google.wear.soyted.horologist.snackbar.SnackbarManager
import com.google.wear.soyted.ui.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rememberWearDao: RememberWearDao,
    private val externalUpdates: ExternalUpdates,
    private val taskEditor: TaskEditor,
    val authRepository: AuthRepository,
    val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val taskId: String = savedStateHandle["taskId"]!!

    val state: StateFlow<TaskScreenState> = rememberWearDao.getTaskAndTaskSeries(taskId)
        .flatMapLatest { task ->
            val notesFlow = task?.taskSeries?.let {
                rememberWearDao.getTaskNotes(it.id)
            } ?: flowOf(null)

            notesFlow.map { notes ->
                TaskScreenState(task, notes)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskScreenState.Empty,
        )

    fun uncomplete(task: Task) {
        viewModelScope.launch {
            try {
                taskEditor.uncomplete(task)
                externalUpdates.forceUpdates()
            } catch (ioe: IOException) {
                snackbarManager.showMessage("Unable to connect to server")
            }
        }
    }

    fun complete(task: Task) {
        viewModelScope.launch {
            try {
                taskEditor.complete(task)
                externalUpdates.forceUpdates()
            } catch (ioe: IOException) {
                snackbarManager.showMessage("Unable to connect to server")
            }
        }
    }
}
