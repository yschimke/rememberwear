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

package com.google.wear.soyted.horologist.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.wear.soyted.horologist.snackbar.material.Snackbar
import com.google.wear.soyted.horologist.snackbar.material.SnackbarHost
import com.google.wear.soyted.horologist.snackbar.material.SnackbarHostState

@Composable
fun DefaultSnackbarHost(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
) {
    SnackbarHost(
        modifier = modifier,
        snackbar = {
            Snackbar(snackbarData = it)
        },
        hostState = hostState,
    )
}
