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

package com.google.wear.rememberwear.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface RememberWearDao {
    @Query("SELECT * FROM taskseries order by created")
    fun getAllTaskSeries(): Flow<List<TaskSeries>>

    @Transaction
    @Query("SELECT * FROM taskseries order by created")
    fun getAllTaskSeriesAndTasks(): Flow<List<TaskSeriesAndTasks>>

    @Transaction
    @Query("SELECT * FROM task order by dueDate, taskSeriesId")
    fun getAllTaskAndTaskSeries(): Flow<List<TaskAndTaskSeries>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTaskSeries(taskSeries: TaskSeries)

    @Query("DELETE FROM taskseries")
    suspend fun deleteAllTaskSeries(): Int

    @Query("SELECT * FROM tag")
    fun allTags(): Flow<List<Tag>>

    @Query("SELECT max(modified) FROM taskseries")
    suspend fun getLastRefresh(): Instant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTag(tag: Tag)

    @Query("SELECT * FROM taskseries where id = :id")
    fun getTaskSeries(id: String): Flow<TaskSeries?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: Note)

    @Query("SELECT * FROM note where taskSeriesId = :taskSeriesId")
    fun getTaskNotes(taskSeriesId: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: Task)

    @Query("SELECT * FROM task where taskSeriesId = :taskSeriesId")
    fun getTasks(taskSeriesId: String): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM task where id = :taskId")
    fun getTaskAndTaskSeries(taskId: String): Flow<TaskAndTaskSeries?>
}