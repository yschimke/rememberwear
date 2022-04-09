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

package com.google.wear.soyted.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation
import com.google.android.horologist.compose.snackbar.SnackbarDuration
import com.google.android.horologist.compose.snackbar.SnackbarHost
import com.google.android.horologist.compose.snackbar.SnackbarHostState

@Composable
fun DialogSnackbarHost(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
) {
    SnackbarHost(
        modifier = modifier.alpha(0.8f),
        snackbar = {
            val duration = when (it.duration) {
                SnackbarDuration.Indefinite -> Long.MAX_VALUE
                SnackbarDuration.Long -> 5000L
                SnackbarDuration.Short -> 2000L
            }
            Confirmation(onTimeout = { it.dismiss() }, durationMillis = duration) {
                Text(text = it.message)
            }
        },
        hostState = hostState,
    )
}
