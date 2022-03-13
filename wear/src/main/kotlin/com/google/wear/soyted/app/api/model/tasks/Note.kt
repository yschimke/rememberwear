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

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import java.time.Instant
import com.google.wear.soyted.app.db.Note as DBNote

@Xml(name = "note")
data class Note(
    @Attribute val id: String,
    @Attribute val created: Instant,
    @Attribute val modified: Instant,
    @Attribute val title: String,
    @TextContent val body: String,
) {
    fun toDBNote(taskSeriesId: String): DBNote =
        DBNote(id, taskSeriesId, created, modified, title, body)
}
