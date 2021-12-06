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

package com.google.wear.rememberwear.tile

import android.content.Context
import android.util.Log
import androidx.wear.tiles.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders.Tile
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.wear.rememberwear.RememberWearActivity
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.kt.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.asListenableFuture
import java.time.Instant
import javax.inject.Inject

const val STABLE_RESOURCES_VERSION = "1"

@AndroidEntryPoint
class RememberWearTileProviderService : androidx.wear.tiles.TileService() {
    // For coroutines, use a custom scope we can cancel when the service is destroyed
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    @Inject
    lateinit var rememberWearDao: RememberWearDao

    override fun onDestroy() {
        super.onDestroy()
        // Cleans up the coroutine
        serviceJob.cancel()
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder().setVersion(STABLE_RESOURCES_VERSION).build()
        )
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> {
        return serviceScope.async {
            suspendTileRequest(requestParams)
        }.asListenableFuture()
    }

    private suspend fun suspendTileRequest(requestParams: RequestBuilders.TileRequest): Tile {
        Log.i("RememberWear", "tileRequest $requestParams")

        val todos = rememberWearDao.getOverdueTaskSeries(Instant.now()).first()

        // TODO Force a refresh if we have stale (> 20 minutes) results or errors

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
                            if (todos.isEmpty()) {
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
                            todos.forEach { todo ->
                                addContent(
                                    text {
                                        setModifiers(
                                            modifiers {
                                                setSemantics(todo.name.toContentDescription())
                                            }
                                        )

                                        setMaxLines(1)
                                        setFontStyle(fontStyle {
                                            setSize(16f.toSpProp())
                                        })
                                        setText(todo.name)
                                    }
                                )
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
