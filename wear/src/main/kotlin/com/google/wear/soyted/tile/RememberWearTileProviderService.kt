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
import androidx.wear.tiles.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.tiles.CoroutinesTileService
import com.google.wear.soyted.RememberWearActivity
import com.google.wear.soyted.db.RememberWearDao
import com.google.wear.soyted.db.TaskAndTaskSeries
import com.google.wear.soyted.kt.activityClickable
import com.google.wear.soyted.kt.column
import com.google.wear.soyted.kt.fontStyle
import com.google.wear.soyted.kt.layout
import com.google.wear.soyted.kt.modifiers
import com.google.wear.soyted.kt.text
import com.google.wear.soyted.kt.tile
import com.google.wear.soyted.kt.timeline
import com.google.wear.soyted.kt.timelineEntry
import com.google.wear.soyted.kt.toContentDescription
import com.google.wear.soyted.kt.toSpProp
import com.google.wear.soyted.util.relativeTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

const val STABLE_RESOURCES_VERSION = "1"

@AndroidEntryPoint
class RememberWearTileProviderService : CoroutinesTileService() {
    @Inject
    lateinit var rememberWearDao: RememberWearDao

    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources =
        ResourceBuilders.Resources.Builder().setVersion(STABLE_RESOURCES_VERSION).build()

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): Tile {
        val today = LocalDate.now()
        val tasks = rememberWearDao.getAllTaskAndTaskSeries().map { tasks ->
            tasks.filter {
                    it.isUrgentUncompleted(today) ||
                            it.isRecentCompleted(today) ||
                            it.isCompletedOn(today)
                }.sortedBy { it.task.dueDate ?: LocalDate.MAX }
            }.first()
        return renderTile(tasks, today)
    }

    private fun renderTile(
        tasks: List<TaskAndTaskSeries>,
        today: LocalDate
    ): Tile {
        return tile {
            setResourcesVersion(STABLE_RESOURCES_VERSION)
            setFreshnessIntervalMillis(0L)

            timeline {
                timelineEntry {
                    layout {
                        column {
                            setHorizontalAlignment(HORIZONTAL_ALIGN_CENTER)

                            setModifiers(
                                modifiers {
                                    setSemantics("Launch RememberWear".toContentDescription())
                                    setClickable(
                                        activityClickable(
                                            this@RememberWearTileProviderService.packageName,
                                            RememberWearActivity::class.java.name
                                        )
                                    )
                                }
                            )
                            if (tasks.isEmpty()) {
                                addContent(
                                    text {
                                        setModifiers(
                                            modifiers {
                                                setSemantics("No overdue tasks".toContentDescription())
                                            }
                                        )

                                        setMaxLines(1)
                                        setFontStyle(fontStyle {
                                            setSize(16f.toSpProp())
                                        })
                                        setText("No overdue tasks")
                                    }
                                )
                            }
                            tasks.forEach { task ->
                                val dueDate = task.task.dueDate
                                addContent(
                                    text {
                                        setModifiers(
                                            modifiers {
                                                setSemantics(task.taskSeries.name.toContentDescription())
                                            }
                                        )

                                        setMaxLines(2)
                                        setFontStyle(fontStyle {
                                            setSize(16f.toSpProp())
                                        })
                                        setText(task.taskSeries.name)
                                    }
                                )
                                if (task.task.dueDate != null) {
                                    val relativeTime = dueDate.relativeTime(today)
                                    addContent(
                                        text {
                                            setModifiers(
                                                modifiers {
                                                    setSemantics(
                                                        relativeTime.toContentDescription()
                                                    )
                                                }
                                            )

                                            setMaxLines(1)
                                            setFontStyle(fontStyle {
                                                setSize(12f.toSpProp())
                                            })
                                            setText(relativeTime)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    companion object {
        fun forceTileUpdate(applicationContext: Context) {
            getUpdater(applicationContext).requestUpdate(RememberWearTileProviderService::class.java)
        }
    }
}
