package com.google.wear.rememberwear.kt

import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ColorBuilders
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TimelineBuilders

fun text(fn: LayoutElementBuilders.Text.Builder.() -> Unit): LayoutElementBuilders.Text {
    val builder = LayoutElementBuilders.Text.Builder()
    fn(builder)
    return builder.build()
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
    val builder = TimelineBuilders.Timeline.Builder()
    builder.fn()
    setTimeline(builder.build())
}

fun column(fn: LayoutElementBuilders.Column.Builder.() -> Unit): LayoutElementBuilders.Column {
    val builder = LayoutElementBuilders.Column.Builder()
    builder.fn()
    return builder.build()
}

fun TimelineBuilders.Timeline.Builder.timelineEntry(fn: TimelineBuilders.TimelineEntry.Builder.() -> Unit) {
    val builder = TimelineBuilders.TimelineEntry.Builder()
    fn(builder)
    addTimelineEntry(builder.build())
}

fun Float.toSpProp() = DimensionBuilders.SpProp.Builder().setValue(this).build()

fun Float.toDpProp() = DimensionBuilders.DpProp.Builder().setValue(this).build()

fun Int.toColorProp(): ColorBuilders.ColorProp =
    ColorBuilders.ColorProp.Builder().setArgb(this).build()

fun String.toContentDescription() =
    ModifiersBuilders.Semantics.Builder().setContentDescription(
        this
    ).build()