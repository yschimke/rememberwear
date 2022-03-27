/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.soyted.app

import android.util.Log
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.wear.soyted.RememberWearActivity
import com.google.wear.soyted.ui.navigation.Screens
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@HiltAndroidTest
class ActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var workMgrRule = WorkManagerRule()

    @get:Rule(order = 2)
    var rule = createAndroidComposeRule<RememberWearActivity>()

    @Test
    fun testEvent() = runTest {
        val scenario = rule.activityRule.scenario

        scenario.moveToState(Lifecycle.State.RESUMED)

        // fails with rule.awaitIdle()
        delay(100)

        toListAndBack()

        scenario.moveToState(Lifecycle.State.STARTED)

        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(100)

        toListAndBack()
    }

    private suspend fun toListAndBack() {
        rule.runOnUiThread {
            rule.activity.navController.navigate(Screens.LoginDialog.route)
        }

        delay(100)

        rule.runOnUiThread {
            rule.activity.navController.popBackStack()
        }
    }
}

class WorkManagerRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                val config = Configuration.Builder()
                    .setMinimumLoggingLevel(Log.DEBUG)
                    .setExecutor(SynchronousExecutor())
                    .build()
                WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
                base.evaluate()
            }
        }
    }
}
