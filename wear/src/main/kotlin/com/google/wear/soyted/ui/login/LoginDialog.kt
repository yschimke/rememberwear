/*
 * Copyright 2022 Google Inc. All rights reserved.
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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.util.rememberStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun LoginDialog(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val loginState by rememberStateWithLifecycle(stateFlow = viewModel.state)

    LaunchedEffect(Unit) {
        viewModel.initiateLogin()
    }

    LaunchedEffect(loginState) {
        if (loginState.state == LoginScreenState.State.Failed || loginState.state == LoginScreenState.State.LoggedIn) {
            delay(3000)
            navController.popBackStack()
        }
    }

    Alert(
        modifier = modifier,
        title = {
            Text("Remember The Milk Login")
        },
    ) {
        item {
            when (loginState.state) {
                LoginScreenState.State.NotLoggedIn -> {
                    // termporary state
                }
                LoginScreenState.State.LoggingIn -> {
                    Text(
                        text = "Waiting for mobile login.",
                        textAlign = TextAlign.Center,
                    )
                }
                LoginScreenState.State.Failed -> {
                    Text(
                        text = "Login Failed\n${loginState.error?.message}",
                        textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
                LoginScreenState.State.LoggedIn -> {
                    Text(
                        text = "Successfully Logged In!.", textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                }
            }
        }
    }
}