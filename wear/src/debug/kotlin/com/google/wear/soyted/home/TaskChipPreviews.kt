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

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.previews.TaskAndSeriesProvider
import com.google.wear.soyted.ui.theme.RememberTheMilkTheme

@Preview(
    widthDp = 228,
    heightDp = 80,
    apiLevel = 26,
    uiMode = Configuration.UI_MODE_TYPE_WATCH,
    backgroundColor = 0xFF000000L
)
@Preview(
    widthDp = 192,
    heightDp = 80,
    apiLevel = 26,
    uiMode = Configuration.UI_MODE_TYPE_WATCH,
    backgroundColor = 0xFF000000L
)
@Composable
fun TaskChipPreview(@PreviewParameter(provider = TaskAndSeriesProvider::class) task: TaskAndTaskSeries) {
    RememberTheMilkTheme {
        TaskChip(task = task, onClick = {})
    }
}