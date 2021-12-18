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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.material.AlertDialog
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.soyted.navigation.Screens
import com.google.wear.soyted.RememberWearViewModel
import com.google.wear.soyted.input.VoicePrompt.voicePromptIntent
import com.google.wear.soyted.input.VoicePrompt.voicePromptLauncher
import com.google.wear.soyted.navigation.NavController
import com.google.wear.soyted.util.LocalRotaryEventDispatcher
import com.google.wear.soyted.util.RotaryEventDispatcher
import com.google.wear.soyted.util.RotaryEventHandlerSetup

val uri = "https://www.rememberthemilk.com/"

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.wear.compose.material.ExperimentalWearMaterialApi::class
)
@Composable
fun RememberWearAppScreens(
    viewModel: RememberWearViewModel
) {
    val rotaryEventDispatcher = remember { RotaryEventDispatcher() }

    val addTaskVoicePrompt = voicePromptLauncher(
        onCreateTask = {
            viewModel.createTask(it)
        }, onError = {
            viewModel.toaster.makeToast(it)
        }
    )

    CompositionLocalProvider(
        LocalRotaryEventDispatcher provides rotaryEventDispatcher
    ) {
        RememberTheMilkTheme {
            val navController = rememberSwipeDismissableNavController()
            val rtmNavController = NavController(navController)

            RotaryEventHandlerSetup(rotaryEventDispatcher)

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
                    AlertDialog(
                        title = {
                            Text("Login")
                        },
                        positiveButton = {
                            Button(onClick = {
                                viewModel.continueLogin()
                                navController.popBackStack()
                            }) {
                                Text("Login")
                            }
                        },
                        negativeButton = {
                        },
                    ) {
                        Text(
                            text = "Continue after login on mobile",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}