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

package com.google.wear.soyted.ui.home

import androidx.lifecycle.viewModelScope
import com.google.wear.soyted.app.work.ExternalUpdates
import com.google.wear.soyted.app.work.TaskCreator
import com.google.wear.soyted.horologist.snackbar.ScaffoldViewModel
import com.google.wear.soyted.horologist.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskCreator: TaskCreator,
    private val externalUpdates: ExternalUpdates,
    snackbarManager: SnackbarManager
) : ScaffoldViewModel(snackbarManager) {
    fun createTask(spokenText: String) {
        viewModelScope.launch {
            try {
                taskCreator.create(spokenText)
                externalUpdates.forceUpdates()
            } catch (ioe: IOException) {
                snackbarManager.showMessage("Unable to connect to server")
            }
        }
    }
}
