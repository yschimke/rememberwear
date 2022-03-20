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

package com.google.wear.soyted.tile.kt

import android.content.Context
import android.graphics.Color
import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ColorBuilders.ColorProp
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TimelineBuilders
import androidx.wear.tiles.material.Button
import androidx.wear.tiles.material.ButtonColors
import androidx.wear.tiles.material.Text
import kotlin.time.Duration

fun text(
    context: Context,
    maxLines: Int? = null,
    typography: Int? = null,
    text: String? = null,
    color: Int? = null,
    fn: Text.Builder.() -> Unit = {}
): Text {
    return Text.Builder(context).apply {
        if (maxLines != null) {
            setMaxLines(maxLines)
        }
        if (typography != null) {
            setTypography(typography)
        }
        if (color != null) {
            setColor(
                ColorProp.Builder()
                    .setArgb(color)
                    .build()
            )
        }
        if (text != null) {
            setModifiers(
                modifiers {
                    setSemantics(text.toContentDescription())
                }
            )

            setText(text)
        }
        fn(this)
    }.build()
}

fun button(
    applicationContext: Context,
    clickable: ModifiersBuilders.Clickable,
    text: String? = null,
    fn: Button.Builder.() -> Unit = {}
): Button {
    return Button.Builder(
        applicationContext,
        clickable
    ).apply {
        if (text != null) {
            setTextContent(text)
        }
        setButtonColors(ButtonColors(Color.TRANSPARENT, Color.RED))
        fn()
    }.build()
}

fun modifiers(fn: ModifiersBuilders.Modifiers.Builder.() -> Unit): ModifiersBuilders.Modifiers {
    val builder = ModifiersBuilders.Modifiers.Builder()
    fn(builder)
    return builder.build()
}

fun activityClickable(
    packageName: String,
    activity: String
) = ModifiersBuilders.Clickable.Builder()
    .setOnClick(
        ActionBuilders.LaunchAction.Builder()
            .setAndroidActivity(
                ActionBuilders.AndroidActivity.Builder()
                    .setPackageName(packageName)
                    .setClassName(activity)
                    .build()
            )
            .build()
    ).build()

fun actionClickable(clickId: String) = ModifiersBuilders.Clickable.Builder()
    .setOnClick(
        ActionBuilders.LoadAction.Builder()
            .build()
    )
    .setId(clickId)
    .build()

fun fontStyle(fn: LayoutElementBuilders.FontStyle.Builder.() -> Unit): LayoutElementBuilders.FontStyle {
    val builder = LayoutElementBuilders.FontStyle.Builder()
    fn(builder)
    return builder.build()
}

fun TimelineBuilders.TimelineEntry.Builder.layout(fn: () -> LayoutElementBuilders.LayoutElement) {
    setLayout(LayoutElementBuilders.Layout.Builder().setRoot(fn()).build())
}

fun tile(fn: TileBuilders.Tile.Builder.() -> Unit): TileBuilders.Tile {
    val builder = TileBuilders.Tile.Builder()
    fn(builder)
    return builder.build()
}

fun TileBuilders.Tile.Builder.timeline(fn: TimelineBuilders.Timeline.Builder.() -> Unit) {
    setTimeline(TimelineBuilders.Timeline.Builder().apply {
        fn()
    }.build())
}

fun row(
    verticalAlign: Int? = null,
    fn: LayoutElementBuilders.Row.Builder.() -> Unit
): LayoutElementBuilders.Row {
    return LayoutElementBuilders.Row.Builder().apply {
        if (verticalAlign != null) {
            setVerticalAlignment(verticalAlign)
        }
        fn()
    }.build()
}

fun column(
    horizontalAlign: Int? = null,
    fn: LayoutElementBuilders.Column.Builder.() -> Unit
): LayoutElementBuilders.Column {
    return LayoutElementBuilders.Column.Builder().apply {
        if (horizontalAlign != null) {
            setHorizontalAlignment(horizontalAlign)
        }
        fn()
    }.build()
}

fun TimelineBuilders.Timeline.Builder.timelineEntry(fn: TimelineBuilders.TimelineEntry.Builder.() -> Unit) {
    val builder = TimelineBuilders.TimelineEntry.Builder()
    fn(builder)
    addTimelineEntry(builder.build())
}

fun tile(
    resourcesVersion: String,
    freshnessInterval: Duration,
    tileLayout: () -> LayoutElementBuilders.LayoutElement
) = tile {
    setResourcesVersion(resourcesVersion)
    setFreshnessIntervalMillis(freshnessInterval.inWholeMilliseconds)

    timeline {
        timelineEntry {
            layout {
                tileLayout()
            }
        }
    }
}

fun String.toContentDescription() =
    ModifiersBuilders.Semantics.Builder().setContentDescription(
        this
    ).build()