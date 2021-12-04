package com.google.wear.rememberwear.db

import android.text.format.DateUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class TaskSeries(
    @PrimaryKey val id: String,
    val name: String,
    val due: Instant
) {
    companion object {
        fun Instant.relativeTime() = DateUtils.getRelativeTimeSpanString(
            toEpochMilli(),
            System.currentTimeMillis(),
            0L,
            DateUtils.FORMAT_ABBREV_ALL
        ).toString()
    }
}
