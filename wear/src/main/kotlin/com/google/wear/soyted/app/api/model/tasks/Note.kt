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
import nl.adaptivity.xmlutil.serialization.XmlValue
import java.time.Instant
import com.google.wear.soyted.app.db.Note as DBNote

@kotlinx.serialization.Serializable
@SerialName("note")
data class Note(
    val id: String,
    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    val created: Instant?,
    @kotlinx.serialization.Serializable(InstantTypeConverter::class)
    val modified: Instant?,
    val title: String,
    @XmlValue(true) val body: String,
) {
    fun toDBNote(taskSeriesId: String): DBNote =
        DBNote(id, taskSeriesId, created, modified, title, body)
}
