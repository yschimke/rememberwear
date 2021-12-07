package com.google.wear.rememberwear.db

import androidx.room.Embedded
import androidx.room.Relation

data class TaskSeriesAndTasks(
    @Embedded val user: TaskSeries,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskSeriesId"
    )
    val tasks: List<Task>
)