/*
 * Copyright 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.soyted.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant
import java.time.LocalDate

@Database(
    entities = [TaskSeries::class, Tag::class, Note::class, Task::class, Auth::class],
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

        @TypeConverter
        fun toDate(dateString: String?): LocalDate? = dateString?.let {
            LocalDate.parse(dateString)
        }

        @TypeConverter
        fun toDateString(date: LocalDate?): String? = date?.toString()
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