/*
 * Copyright 2022 Google Inc. All rights reserved.
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
import android.graphics.Color
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.material.CompactChip
import androidx.wear.tiles.material.Typography
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.google.wear.soyted.RememberWearActivity
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.tile.kt.STABLE_RESOURCES_VERSION
import com.google.wear.soyted.tile.kt.actionClickable
import com.google.wear.soyted.tile.kt.activityClickable
import com.google.wear.soyted.tile.kt.button
import com.google.wear.soyted.tile.kt.column
import com.google.wear.soyted.tile.kt.row
import com.google.wear.soyted.tile.kt.text
import com.google.wear.soyted.tile.kt.tile
import com.google.wear.soyted.ui.util.relativeTime
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

fun Context.renderTile(
    requestParams: RequestBuilders.TileRequest,
    tasks: List<TaskAndTaskSeries>,
    today: LocalDate
): TileBuilders.Tile {
    return tile(
        resourcesVersion = STABLE_RESOURCES_VERSION,
        freshnessInterval = 15.minutes
    ) {
        tileLayout(requestParams, tasks, today)
    }
}

fun Context.tileLayout(
    requestParams: RequestBuilders.TileRequest,
    tasks: List<TaskAndTaskSeries>,
    today: LocalDate
): LayoutElementBuilders.LayoutElement {
    return PrimaryLayout.Builder(requestParams.deviceParameters!!)
        .setContent(bodyLayout(tasks, today))
        .setPrimaryChipContent(actionChip(requestParams))
        .build()
}

fun Context.actionChip(requestParams: RequestBuilders.TileRequest): LayoutElementBuilders.LayoutElement =
    CompactChip.Builder(
        this.applicationContext,
        "Open",
        activityClickable(
            packageName,
            RememberWearActivity::class.java.name
        ),
        requestParams.deviceParameters!!
    ).build()

fun Context.bodyLayout(
    tasks: List<TaskAndTaskSeries>,
    today: LocalDate
) = column(horizontalAlign = LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER) {
    if (tasks.isEmpty()) {
        addContent(
            emptyNotice()
        )
    } else {
        tasks.forEach { task ->
            addContent(
                taskRow(task, today)
            )
        }
    }
}

fun Context.taskRow(
    task: TaskAndTaskSeries,
    today: LocalDate
) = row {
    addContent(
        column {
            addContent(
                text(
                    this@taskRow,
                    text = task.taskSeries.name,
                    maxLines = 2,
                    color = Color.WHITE,
                    typography = Typography.TYPOGRAPHY_BODY2
                )
            )

            if (task.task.dueDate != null) {
                val relativeTime = task.task.dueDate.relativeTime(today)
                addContent(
                    text(
                        applicationContext,
                        text = relativeTime,
                        maxLines = 1,
                        color = Color.LTGRAY,
                        typography = Typography.TYPOGRAPHY_CAPTION2
                    )
                )
            }
        }
    )
    addContent(
        button(
            applicationContext,
            actionClickable((if (task.isCompleted) "uncomplete:" else "complete:") + task.task.id),
            text = if (task.isCompleted) "✅" else "⭕"
        )
    )
}

fun Context.emptyNotice() = text(
    this,
    text = "No overdue tasks",
    maxLines = 3,
    typography = Typography.TYPOGRAPHY_DISPLAY1
)