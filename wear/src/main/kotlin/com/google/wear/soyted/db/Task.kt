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

package com.google.wear.soyted.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = TaskSeries::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("taskSeriesId"),
            onDelete = CASCADE
        )
    ),
    indices = arrayOf(Index("taskSeriesId"))
)
data class Task(
    @PrimaryKey val id: String,
    val taskSeriesId: String,
    val dueDate: LocalDate?,
    val added: Instant,
    val completed: Instant?,
    val deleted: Instant?,
    val edited: Boolean,
    val priority: String,
    val postponed: String,
    val estimate: String,
)
