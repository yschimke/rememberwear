package com.google.wear.rememberwear.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tickaroo.tikxml.annotation.Attribute
import java.time.Instant

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
    val due: Instant?,
    val added: Instant,
    val completed: Instant?,
    val deleted: Instant?,
    val priority: String,
    val postponed: String,
    val estimate: String,
)
