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

package com.google.wear.soyted.app.api.model.tasks

import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlElement

@kotlinx.serialization.Serializable
@SerialName("tasks")
data class Tasks(
    var rev: String,

    @XmlElement(true)
    var list: List<TaskList>?
) {
    val taskSeries: List<TaskSeries>
        get() = list?.flatMap { it.taskseries.orEmpty() }.orEmpty()
}
