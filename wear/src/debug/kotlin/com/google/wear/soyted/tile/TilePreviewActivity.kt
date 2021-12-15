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

package com.google.wear.soyted.tile

import android.content.ComponentName
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.tiles.manager.TileUiClient

class TilePreviewActivity : ComponentActivity() {
    private lateinit var tileUiClient: TileUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                FrameLayout(context).also { frameLayout ->
                    tileUiClient = TileUiClient(
                        context = this,
                        component = ComponentName(this, RememberWearTileProviderService::class.java),
                        parentView = frameLayout
                    )
                    tileUiClient.connect()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tileUiClient.close()
    }
}
