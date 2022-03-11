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

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.ui.InboxScreen
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.locale.LocaleTestRule

class ScreengrabTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenshot() {
        composeTestRule.setContent {
            InboxScreen(
                tasks = TaskAndSeriesProvider.taskAndTaskSeries,
                onClick = { },
                voicePromptQuery = { },
                loginAction = { },
                isLoggedIn = true
            )
        }

        composeTestRule.onRoot().assertIsDisplayed()

        composeTestRule.onRoot().captureToImage()

        val context = InstrumentationRegistry.getInstrumentation().context
        val isScreenRound = context.resources.configuration.isScreenRound
        Screengrab.screenshot("home_wear", WearScreenshotStrategy(isRoundDevice = isScreenRound))
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