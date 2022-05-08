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

import com.google.wear.soyted.app.api.model.util.InstantTypeConverter
import kotlinx.serialization.SerialName
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.Instant

@kotlinx.serialization.Serializable
@SerialName("taskseries")
data class TaskSeries(
    var id: String,

    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    var created: Instant?,

    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    var modified: Instant?,

    var name: String,

    var source: String,

    var url: String,

    var location_id: String,

    @XmlElement(true)
    @XmlChildrenName("tag", "", "")
    var tags: List<Tag>?,

//    @Element
//    var participants: MutableList<Task> = mutableListOf(),

    @XmlElement(true)
    @XmlChildrenName("note", "", "")
    var notes: MutableList<Note>?,

    @XmlElement(true)
    var task: List<Task>?,

    @XmlElement(true)
    var rrule: RRule?,
) {
    fun toDBTaskSeries(listId: String): com.google.wear.soyted.app.db.TaskSeries {
        return com.google.wear.soyted.app.db.TaskSeries(
            id = id,
            listId = listId,
            name = name,
            created = created,
            modified = modified,
            isRepeating = rrule != null
        )
    }
}
