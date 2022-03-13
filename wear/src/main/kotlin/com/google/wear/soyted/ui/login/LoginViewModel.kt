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

package com.google.wear.soyted.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.soyted.app.work.ScheduledWork
import com.google.wear.soyted.horologist.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val scheduledWork: ScheduledWork,
    val authRepository: AuthRepository,
    private val snackbarManager: SnackbarManager,
    val loginFlow: LoginFlow
) : ViewModel() {
    fun continueLogin(onLogin: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                loginFlow.enterToken()

                onLogin()

                scheduledWork.refetchAllDataWork()
            } catch (e: Exception) {
                snackbarManager.showMessage("Login failed: ${e.message}")
            }
        }
    }

    fun startLoginFlow() {
        viewModelScope.launch {
            loginFlow.startLogin()
        }
    }
}
