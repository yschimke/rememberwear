package com.google.wear.rememberwear.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Todo(
    @PrimaryKey val id: String,
    val title: String,
    val due: Instant
) {
}
