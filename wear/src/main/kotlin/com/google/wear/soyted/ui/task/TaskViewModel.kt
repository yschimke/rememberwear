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
import com.google.wear.soyted.app.work.TaskEditor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rememberWearDao: RememberWearDao,
    private val taskEditor: TaskEditor,
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
