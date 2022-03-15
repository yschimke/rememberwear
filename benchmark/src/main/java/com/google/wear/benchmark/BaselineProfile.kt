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

package com.google.wear.benchmark

import android.graphics.Point
import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.StaleObjectException
import org.junit.Rule
import org.junit.Test

@LargeTest
@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfile {
    @get:Rule
    val baselineRule = BaselineProfileRule()

    @Test
    fun profile() {
        baselineRule.collectBaselineProfile(
            packageName = "ee.schimke.wear.soyted",
            profileBlock = {
                startActivityAndWait {
                    it.action = "com.google.wear.soyted.ACTIVITY"
                }

                repeat(5) {
                    println("Iteration $it")
                    try {
                        val column = device.findObject(By.desc("Inbox List"))

                        if (column != null) {
                            column.setGestureMargin(device.displayWidth / 5)
                            column.drag(Point(column.visibleCenter.x, column.visibleCenter.y / 3))
                            device.waitForIdle()

                            column.drag(
                                Point(
                                    column.visibleCenter.x,
                                    column.visibleCenter.y + column.visibleCenter.y / 3
                                )
                            )
                            device.waitForIdle()
                        }
                    } catch (soe: StaleObjectException) {
                        //ignore for now
                        println(soe)
                    }
                    device.pressBack()
                }
            }
        )
    }
}
