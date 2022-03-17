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

package com.google.wear.soyted.fastlane

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeSource
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.ui.inbox.InboxScreen
import com.google.wear.soyted.ui.task.TaskScreen
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.locale.LocaleTestRule
import java.time.LocalDate

class ScreengrabTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenshot() = screenshot("home_wear") {
        InboxScreen(
            tasks = TaskAndSeriesProvider.taskAndTaskSeries,
            onClick = { },
            voicePromptQuery = { },
            loginAction = { },
            isLoggedIn = true,
            focusRequester = remember { FocusRequester() }
        )
    }

    @Test
    fun taskScreenshot() = screenshot("task_wear") {
        val task = TaskAndSeriesProvider.taskAndTaskSeries.first()

        TaskScreen(
            focusRequester = remember { FocusRequester() },
            scrollState = rememberScalingLazyListState(),
            taskSeries = task.taskSeries,
            task = task.task,
            today = LocalDate.of(2021, 2, 18),
            notes = listOf(),
            onComplete = {
            },
            onUncomplete = {
            }
        )
    }

    object FixedTimeSource: TimeSource {
        override val currentTime: String
            @Composable get() = "10:10"
    }

    private fun screenshot(screenshotName: String, block: @Composable () -> Unit) {
        composeTestRule.setContent {
            RememberTheMilkTheme {
                Scaffold(timeText = {
                    TimeText(timeSource = FixedTimeSource)
                }) {
                    block()
                }
            }
        }

        composeTestRule.onRoot().assertIsDisplayed()
        composeTestRule.onRoot().captureToImage()

        val context = InstrumentationRegistry.getInstrumentation().context
        val isScreenRound = context.resources.configuration.isScreenRound
        Screengrab.screenshot(screenshotName, WearScreenshotStrategy(isRoundDevice = isScreenRound))
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun configure() {
            val context = InstrumentationRegistry.getInstrumentation().context
            val isScreenRound = context.resources.configuration.isScreenRound

            Screengrab.setDefaultScreenshotStrategy(WearScreenshotStrategy(isScreenRound))
        }

        @ClassRule
        @JvmField
        val localeTestRule = LocaleTestRule()
    }
}
