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

package com.google.wear.soyted.tile

import android.content.Context
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.wear.soyted.app.db.RememberWearDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class RememberWearTileProviderService : SuspendingTileService() {
    private lateinit var renderer: RememberWearTileRenderer

    @Inject
    lateinit var rememberWearDao: RememberWearDao


    override fun onCreate() {
        super.onCreate()

        renderer = RememberWearTileRenderer(this)
    }


    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        return renderer.produceRequestedResources(
            resourceState = Unit,
            requestParams = requestParams
        )
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): Tile {
        val lastClickableId = requestParams.currentState.lastClickableId
        if (lastClickableId.isNotEmpty()) {
            handleClick(lastClickableId)
        }

        val today = LocalDate.now()
        val tasks = getStablePrioritisedTasks(today)
        val data = RememberWearTileRenderer.TileData(today = today, tasks = tasks)

        return renderer.renderTimeline(data, requestParams)
    }

    private suspend fun getStablePrioritisedTasks(today: LocalDate?) =
        rememberWearDao.getAllTaskAndTaskSeries().map { tasks ->
            tasks
                .filter {
                    it.isUrgentUncompleted(today) ||
                            it.isCompletedOn(today)
                }
                .sortedBy { it.task.dueDate ?: LocalDate.MAX }
                .take(2)
        }.first()

    suspend fun handleClick(lastClickableId: String) {
        val (command, id) = lastClickableId.split(":")

        val task = rememberWearDao.getTaskAndTaskSeries(id).first()

        if (task != null) {
            if (command == "complete") {
                rememberWearDao.upsertTask(task.task.copy(completed = Instant.now()))
            } else {
                rememberWearDao.upsertTask(task.task.copy(completed = null))
            }
        }
    }


    companion object {
        fun forceTileUpdate(applicationContext: Context) {
            getUpdater(applicationContext).requestUpdate(RememberWearTileProviderService::class.java)
        }
    }
}
