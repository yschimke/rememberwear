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

package com.google.wear.remember.wear.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.wear.rememberwear.api.RememberTheMilkService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RememberTheMilkApiTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Inject
    lateinit var service: RememberTheMilkService

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun tasks() {
        runBlocking(testScope.coroutineContext) {
            val tasks = service.tasks()
            assertThat(tasks.tasks?.taskSeries).isNotEmpty
        }
    }

    @Test
    fun tasksInWearTag() {
        runBlocking(testScope.coroutineContext) {
            val tasks = service.tasks("tag:wear")
            assertThat(tasks.tasks?.taskSeries).isNotEmpty
        }
    }

    @Test
    fun lists() {
        runBlocking(testScope.coroutineContext) {
            val lists = service.lists()
            assertThat(lists).isNotNull
        }
    }

    @Test
    fun tags() {
        runBlocking(testScope.coroutineContext) {
            val tags = service.tags()
            assertThat(tags.tags).isNotEmpty
        }
    }
}