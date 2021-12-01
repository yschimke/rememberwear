package com.google.wear.rememberwear.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class TaskSeries(
    @PrimaryKey val id: String,
    val name: String,
    val due: Instant?,
    val created: Instant,
    val modified: Instant,
    val isRepeating: Boolean
)
