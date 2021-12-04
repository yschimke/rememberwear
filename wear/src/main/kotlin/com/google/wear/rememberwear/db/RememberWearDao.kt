package com.google.wear.rememberwear.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RememberWearDao {
    @Query("SELECT * FROM taskseries")
    fun getAllTodos(): Flow<List<TaskSeries>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(taskSeries: TaskSeries)

    @Query("DELETE FROM taskseries")
    fun deleteAllTodos()
}