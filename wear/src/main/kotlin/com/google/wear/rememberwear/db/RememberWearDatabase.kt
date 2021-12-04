package com.google.wear.rememberwear.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant

@Database(
    entities = [TaskSeries::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(RememberWearDatabase.Converters::class)
abstract class RememberWearDatabase : RoomDatabase() {
    abstract fun rememberWearDao(): RememberWearDao

    class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Instant? {
            return value?.let { Instant.ofEpochMilli(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Instant?): Long? {
            return date?.toEpochMilli()
        }
    }

    companion object {
        private lateinit var INSTANCE: RememberWearDatabase

        fun getDatabase(context: Context): RememberWearDatabase {
            return synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    val instance = Room.databaseBuilder(
                        context,
                        RememberWearDatabase::class.java,
                        "rtm"
                    )
                        // TODO support migrations
                        .fallbackToDestructiveMigration()
                        .enableMultiInstanceInvalidation()
                        .build()
                    INSTANCE = instance
                }

                INSTANCE
            }
        }
    }
}