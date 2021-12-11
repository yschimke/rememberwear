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
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RememberWearDatabaseTest {
    private lateinit var mediaDao: RememberWearDao
    private lateinit var db: RememberWearDatabase

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RememberWearDatabase::class.java
        ).build()
        mediaDao = db.rememberWearDao()
    }

    @After
    fun tearDown() {
        db.close()
        testDispatcher.cancel()
    }

    @Test
    fun useInsertAndRead() {
        // TODO debug and use runTestBlocking
        runBlocking(testScope.coroutineContext) {
            val now = Instant.now()
            val todo = TaskSeries("1", "all", "Tablets", now, now, false)

            mediaDao.upsertTaskSeries(todo)

            val todoCopy = mediaDao.getAllTaskSeries().first().single()

            assertEquals(todo, todoCopy)
        }
    }
}