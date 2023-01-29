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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.compose.navscaffold.WearNavScaffold
import com.google.android.horologist.compose.navscaffold.composable
import com.google.android.horologist.compose.navscaffold.scrollable
import com.google.wear.soyted.snackbar.DialogSnackbarHost
import com.google.wear.soyted.ui.inbox.InboxScreen
import com.google.wear.soyted.ui.input.VoicePrompt
import com.google.wear.soyted.ui.login.LoginDialog
import com.google.wear.soyted.ui.navigation.NavController
import com.google.wear.soyted.ui.navigation.Screens
import com.google.wear.soyted.ui.task.TaskScreen
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme

val uri = "https://www.rememberthemilk.com/"

@Composable
fun RememberWearAppScreens(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController = rememberSwipeDismissableNavController(),
) {
    val addTaskVoicePrompt = VoicePrompt.voicePromptLauncher(
        onCreateTask = {
            viewModel.createTask(it)
        },
        onError = {
            viewModel.showMessage(it)
        }
    )

    RememberTheMilkTheme {
        val rtmNavController = NavController(navController)

        WearNavScaffold(
            modifier = Modifier.fillMaxSize(),
            startDestination = Screens.Inbox.route,
            navController = navController,
            snackbar = {
                DialogSnackbarHost(
                    modifier = Modifier.fillMaxSize(),
                    hostState = viewModel.snackbarHostState
                )
            }
        ) {
            scrollable(
                route = Screens.Inbox.route,
                deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/" }),
            ) {
                InboxScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = rtmNavController,
                    addTaskVoicePrompt = addTaskVoicePrompt,
                    columnState = it.columnState,
                )
            }

            scrollable(
                route = Screens.Task.route + "/{taskId}",
                arguments = listOf(
                    navArgument("taskId", builder = {
                        this.type = NavType.StringType
                    })
                ),
                deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/#all/{taskId}" }),
            ) {
                TaskScreen(
                    navController = rtmNavController,
                    columnState = it.columnState,
                )
            }

            composable(Screens.LoginDialog.route) {
                LoginDialog(
                    navController = rtmNavController
                )
            }
        }
    }
}
