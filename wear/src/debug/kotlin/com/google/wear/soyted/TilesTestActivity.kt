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

package com.google.wear.soyted

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.StateBuilders.State
import androidx.wear.tiles.renderer.TileRenderer
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.tile.buildResources
import com.google.wear.soyted.tile.renderTile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.time.LocalDate
import kotlin.math.roundToInt

class TilesTestActivity : ComponentActivity() {
    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout = FrameLayout(this)
        setContentView(frameLayout)

        renderTile()
    }

    fun renderTile() {
        val requestParams = requestParams()
        val tile =
            renderTile(requestParams, TaskAndSeriesProvider.taskAndTaskSeries, LocalDate.now())

        val tileRenderer = TileRenderer(
            /* uiContext = */ this,
            /* layout = */ tile.timeline?.timelineEntries?.first()?.layout!!,
            /* resources = */ buildResources(),
            /* loadActionExecutor = */ Dispatchers.IO.asExecutor(),
            /* loadActionListener = */ {}
        )

        tileRenderer.inflate(frameLayout)
    }

    private fun requestParams() = TileRequest.Builder()
        .setDeviceParameters(buildDeviceParameters())
        .setState(State.Builder().build())
        .build()

    fun buildDeviceParameters(): DeviceParametersBuilders.DeviceParameters {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val isScreenRound: Boolean = resources.configuration.isScreenRound
        return DeviceParametersBuilders.DeviceParameters.Builder()
            .setScreenWidthDp((displayMetrics.widthPixels / displayMetrics.density).roundToInt())
            .setScreenHeightDp((displayMetrics.heightPixels / displayMetrics.density).roundToInt())
            .setScreenDensity(displayMetrics.density)
            .setScreenShape(
                if (isScreenRound) DeviceParametersBuilders.SCREEN_SHAPE_ROUND
                else DeviceParametersBuilders.SCREEN_SHAPE_RECT
            )
            .setDevicePlatform(DeviceParametersBuilders.DEVICE_PLATFORM_WEAR_OS)
            .build()
    }
}
