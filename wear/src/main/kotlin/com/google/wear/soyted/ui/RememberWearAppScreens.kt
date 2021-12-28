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

package com.google.wear.soyted.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.material.AlertDialog
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.soyted.RememberWearViewModel
import com.google.wear.soyted.input.VoicePrompt.voicePromptIntent
import com.google.wear.soyted.input.VoicePrompt.voicePromptLauncher
import com.google.wear.soyted.navigation.NavController
import com.google.wear.soyted.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val uri = "https://www.rememberthemilk.com/"

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.wear.compose.material.ExperimentalWearMaterialApi::class
)
@Composable
fun RememberWearAppScreens(
    viewModel: RememberWearViewModel
) {
    val addTaskVoicePrompt = voicePromptLauncher(
        onCreateTask = {
            viewModel.createTask(it)
        }, onError = {
            viewModel.toaster.makeToast(it)
        }
    )

    RememberTheMilkTheme {
        val navController = rememberSwipeDismissableNavController()
        val rtmNavController = NavController(navController)

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Screens.Inbox.route
        ) {
            composable(
                Screens.Inbox.route,
                deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/" })
            ) {
                InboxScreen(
                    viewModel = viewModel,
                    onClick = {
                        rtmNavController.navigateToTask(it.task.id)
                    },
                    voicePromptQuery = {
                        addTaskVoicePrompt.launch(voicePromptIntent)
                    },
                    loginAction = {
                        viewModel.startLoginFlow()
                        rtmNavController.navigateToLoginDialog()
                    }
                )
            }

            composable(
                Screens.Task.route + "/{taskId}", arguments = listOf(
                    navArgument("taskId", builder = {
                        this.type = NavType.StringType
                    })
                ), deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/#all/{taskId}" })
            ) {
                val taskId = it.arguments?.getString("taskId")
                TaskScreen(viewModel = viewModel, taskId = taskId!!, navController = navController)
            }

            composable(
                Screens.LoginDialog.route
            ) {
                LogingDialog(viewModel, navController)
            }
        }
    }
}

@Composable
private fun LogingDialog(
    viewModel: RememberWearViewModel,
    navController: NavHostController
) {
    AlertDialog(
        title = {
            Text("Login")
        },
    ) {
        Text(
            text = "Continue after login on mobile",
            textAlign = TextAlign.Center
        )
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