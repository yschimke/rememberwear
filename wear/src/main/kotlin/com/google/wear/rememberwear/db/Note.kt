package com.google.wear.rememberwear.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tickaroo.tikxml.annotation.Attribute
import java.time.Instant

@Entity
data class Note(
    @PrimaryKey val id: String,
    val taskSeriesId: String,
    val created: Instant,
    val modified: Instant,
    val title: String,
    val body: String
)