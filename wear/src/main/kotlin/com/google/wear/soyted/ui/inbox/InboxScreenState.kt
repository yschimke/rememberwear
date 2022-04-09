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

package com.google.wear.soyted.ui.inbox

import com.google.wear.soyted.app.db.TaskAndTaskSeries
import java.time.LocalDate

data class InboxScreenState(
    val tasks: List<TaskAndTaskSeries>? = null,
    val today: LocalDate = LocalDate.now(),
    val isRefreshing: Boolean = false,
    val isLoggedIn: Boolean? = null
) {
    companion object {
        val Empty = InboxScreenState()
    }
}

