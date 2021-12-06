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

package com.google.wear.rememberwear.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.rememberwear.navigation.Screens
import com.google.wear.rememberwear.RememberWearViewModel

val uri = "https://www.rememberthemilk.com/"

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.wear.compose.material.ExperimentalWearMaterialApi::class
)
@Composable
fun CircleAppScreens(viewModel: RememberWearViewModel) {
    RememberTheMilkTheme {
        val navController = rememberSwipeDismissableNavController()

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Screens.Inbox.route
        ) {
            composable(
                Screens.Inbox.route,
                deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/" })
            ) {
                InboxScreen(viewModel = viewModel, onClick = {
                    println("Click " + Screens.TaskSeries.route + "/" + it.id)
                    navController.navigate(Screens.TaskSeries.route + "/" + it.id)
                })
            }

            composable(
                Screens.TaskSeries.route + "/{taskSeriesId}", arguments = listOf(
                    navArgument("taskSeriesId", builder = {
                        this.type = NavType.StringType
                    })
                ), deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/#all/{taskSeriesId}" })
            ) {
                val taskSeriesId = it.arguments?.getString("taskSeriesId")
                TaskSeriesScreen(viewModel = viewModel, taskSeriesId = taskSeriesId!!)
            }
        }
    }

}