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

package com.google.wear.soyted.ui.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.soyted.horologist.snackbar.material.SnackbarHost
import com.google.wear.soyted.ui.inbox.InboxScreen
import com.google.wear.soyted.ui.input.VoicePrompt
import com.google.wear.soyted.ui.login.LoginViewModel
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.navigation.Screens
import com.google.wear.soyted.ui.task.TaskScreen
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val uri = "https://www.rememberthemilk.com/"

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.wear.compose.material.ExperimentalWearMaterialApi::class
)
@Composable
fun RememberWearAppScreens(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val addTaskVoicePrompt = VoicePrompt.voicePromptLauncher(onCreateTask = {
        viewModel.createTask(it)
    }, onError = {
        viewModel.showMessage(it)
    })

    RememberTheMilkTheme {
        val navController = rememberSwipeDismissableNavController()
        val rtmNavController = NavController(navController)

        Scaffold(timeText = { TimeText() }) {
            Box {
                SwipeDismissableNavHost(
                    navController = navController, startDestination = Screens.Inbox.route
                ) {
                    composable(
                        Screens.Inbox.route,
                        deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/" })
                    ) {
                        InboxScreen(
                            navController = rtmNavController,
                            addTaskVoicePrompt = addTaskVoicePrompt
                        )
                    }

                    composable(
                        Screens.Task.route + "/{taskId}", arguments = listOf(
                            navArgument("taskId", builder = {
                                this.type = NavType.StringType
                            })
                        ), deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/#all/{taskId}" })
                    ) {
                        TaskScreen(navController = rtmNavController)
                    }

                    composable(
                        Screens.LoginDialog.route
                    ) {
                        LoginDialog(navController = rtmNavController)
                    }
                }

                SnackbarHost(hostState = viewModel.snackbarHostState)
            }
        }
    }
}

@Composable
private fun LoginDialog(
    viewModel: LoginViewModel = hiltViewModel(), navController: NavController
) {
    Alert(
        title = {
            Text("Login")
        },
    ) {
        item {
            Text(
                text = "Continue after login on mobile", textAlign = TextAlign.Center
            )
        }
        item {
            Button(onClick = {
                viewModel.continueLogin {
                    withContext(Dispatchers.Main) {
                        navController.popBackStack()
                    }
                }
            }) {
                Text("Login")
            }
        }
    }
}