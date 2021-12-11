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

package com.google.wear.soyted.previews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.google.wear.soyted.ui.RememberTheMilkTheme
import java.time.LocalDate
import java.time.ZoneOffset

object Previews {
    val localDateTime = LocalDate.now().atTime(12, 35)
    val timestamp = localDateTime.toInstant(ZoneOffset.UTC)
}

@Composable
fun RememberTheMilkThemePreview(
    round: Boolean = true,
    content: @Composable () -> Unit,
) {
    RememberTheMilkTheme {
        if (round) {
            val configuration =
                LocalConfiguration.current.let {
                    Configuration(it).apply {
                        screenLayout =
                            (screenLayout or Configuration.SCREENLAYOUT_ROUND_YES) //xor Configuration.SCREENLAYOUT_ROUND_YES
                    }
                }

            CompositionLocalProvider(LocalConfiguration provides configuration) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                ) {
                    content()
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                content()
            }
        }
    }
}