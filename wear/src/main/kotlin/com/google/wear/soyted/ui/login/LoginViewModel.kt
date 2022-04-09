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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val loginFlow: LoginFlow,
) : ViewModel() {
    val state = MutableStateFlow(LoginScreenState())

    fun initiateLogin() {
        viewModelScope.launch {
            val error = loginFlow.startLogin()

            if (error != null) {
                state.value = LoginScreenState(
                    LoginScreenState.State.Failed,
                    error = error
                )
            } else {
                state.value = LoginScreenState(LoginScreenState.State.LoggingIn)
            }

            val error2 = loginFlow.waitForToken()

            if (error2 != null) {
                state.value = LoginScreenState(
                    LoginScreenState.State.Failed,
                    error = error2
                )
            } else {
                state.value = LoginScreenState(LoginScreenState.State.LoggedIn)
            }
        }
    }

    fun abortLogin() {
        // nothing for now
    }
}
