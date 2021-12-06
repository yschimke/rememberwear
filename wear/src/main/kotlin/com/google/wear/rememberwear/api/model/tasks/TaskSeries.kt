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

package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml
import java.time.Instant

@Xml(name = "taskseries")
data class TaskSeries(
    @Attribute
    var id: String,

    @Attribute
    var created: Instant,

    @Attribute
    var modified: Instant,

    @Attribute
    var name: String,

    @Attribute
    var source: String,

    @Attribute
    var url: String,

    @Attribute
    var location_id: String,

    @Path("tags")
    @Element
    var tags: List<Tag>?,

//    @Element
//    var participants: MutableList<Task> = mutableListOf(),

    @Path("notes")
    @Element
    var notes: MutableList<Note>?,

    @Element
    var task: List<Task>?,

    @Element
    var rrule: RRule?,
)
