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

package com.google.wear.soyted

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.soyted.ui.home.RememberWearAppScreens
import com.google.wear.soyted.ui.util.JankPrinter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RememberWearActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private lateinit var jankPrinter: JankPrinter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jankPrinter = JankPrinter()

        setContent {
            navController = rememberSwipeDismissableNavController()

            val context = LocalContext.current
            val isWatch =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH)
            if (isWatch) {
                RememberWearAppScreens(navController = navController)

                LaunchedEffect(Unit) {
                    navController.currentBackStackEntryFlow.collect {
                        jankPrinter.setRouteState(route = it.destination.route)
                    }
                }
            } else {
                NotAWatch()
            }
        }

        jankPrinter.installJankStats(activity = this)
    }

}

@Composable fun NotAWatch() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Soyted is a Wear OS App")
    }
}
