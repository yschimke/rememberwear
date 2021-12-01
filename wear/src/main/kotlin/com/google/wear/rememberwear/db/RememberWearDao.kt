package com.google.wear.rememberwear.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface RememberWearDao {
    @Query("SELECT * FROM taskseries order by due")
    fun getAllTaskSeries(): Flow<List<TaskSeries>>

    @Query("SELECT * FROM taskseries where due < :time order by due")
    fun getOverdueTaskSeries(time: Instant): Flow<List<TaskSeries>>

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
}