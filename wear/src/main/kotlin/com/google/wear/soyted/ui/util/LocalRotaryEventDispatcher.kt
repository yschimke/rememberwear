/*
 * Copyright 2021-2022 Google Inc. All rights reserved.
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


package com.google.wear.soyted.ui.util

import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.InputDeviceCompat
import androidx.core.view.MotionEventCompat
import kotlinx.coroutines.launch

fun Modifier.scrollHandler(scrollState: ScrollableState): Modifier = composed {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaledVerticalScrollFactor =
        remember { ViewConfiguration.get(context).scaledVerticalScrollFactor }

    this.pointerInteropFilter(RequestDisallowInterceptTouchEvent()) { event ->
        if (event.action != MotionEvent.ACTION_SCROLL ||
            !event.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
        ) {
            false
        } else {
            val delta = -event.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                    scaledVerticalScrollFactor
            scope.launch {
                scrollState.scrollBy(delta)
            }
            true
        }
    }
}
