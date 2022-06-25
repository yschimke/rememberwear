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

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.wear.soyted.app.db.Task
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.app.db.TaskSeries
import com.google.wear.soyted.previews.SampleData.localDateTime
import com.google.wear.soyted.previews.SampleData.taskAndTaskSeries
import com.google.wear.soyted.previews.SampleData.timestamp
import java.time.Instant
import java.time.LocalDate

class TaskAndSeriesProvider : PreviewParameterProvider<TaskAndTaskSeries> {
    override val values: Sequence<TaskAndTaskSeries> = taskAndTaskSeries.asSequence()

    companion object {

    }
}
