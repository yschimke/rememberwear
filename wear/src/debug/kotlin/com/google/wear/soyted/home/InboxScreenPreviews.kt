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

package com.google.wear.soyted.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.wear.soyted.ui.inbox.InboxScreen
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true
)
@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true
)
@Composable
fun CirclesListPreview() {
    RememberTheMilkTheme {
        InboxScreen(
            tasks = TaskAndSeriesProvider.taskAndTaskSeries,
            onClick = {},
            voicePromptQuery = {},
            loginAction = {},
            isLoggedIn = true
        )
    }
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true
)
@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true
)
@Composable
fun CirclesListNotLoggedInPreview() {
    RememberTheMilkTheme {
        InboxScreen(
            tasks = listOf(),
            onClick = {},
            voicePromptQuery = {},
            loginAction = {},
            isLoggedIn = false
        )
    }
}