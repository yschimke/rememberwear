package com.google.wear.rememberwear.db

import android.content.Context
import android.location.Location
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant

@Database(
    entities = [Todo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() {
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
        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            return synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    val instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
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