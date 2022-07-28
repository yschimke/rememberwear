package com.google.wear.soyted.tile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.DeviceParametersBuilders.DeviceParameters
import androidx.wear.tiles.DimensionBuilders.DpProp
import androidx.wear.tiles.LayoutElementBuilders.Column
import androidx.wear.tiles.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import androidx.wear.tiles.LayoutElementBuilders.Spacer
import androidx.wear.tiles.ModifiersBuilders.Clickable
import androidx.wear.tiles.ModifiersBuilders.Modifiers
import androidx.wear.tiles.ModifiersBuilders.Semantics
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.material.Chip
import androidx.wear.tiles.material.ChipColors
import androidx.wear.tiles.material.Colors
import androidx.wear.tiles.material.CompactChip
import androidx.wear.tiles.material.Text
import androidx.wear.tiles.material.Typography
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.google.android.horologist.compose.tools.ExperimentalHorologistComposeToolsApi
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.google.wear.soyted.R
import com.google.wear.soyted.RememberWearActivity
import com.google.wear.soyted.app.db.TaskAndTaskSeries
import com.google.wear.soyted.previews.SampleData
import com.google.wear.soyted.previews.WearPreviewDevices
import com.google.wear.soyted.previews.WearPreviewFontSizes
import com.google.wear.soyted.ui.theme.RememberTheMilkColorPalette
import com.google.wear.soyted.ui.util.relativeTime
import java.time.LocalDate

class RememberWearTileRenderer(context: Context) :
    SingleTileLayoutRenderer<RememberWearTileRenderer.TileData, Unit>(context) {
    data class TileData(
        val today: LocalDate,
        val tasks: List<TaskAndTaskSeries>
    )

    override fun createTheme(): Colors {
        return Colors(
            RememberTheMilkColorPalette.primary.toArgb(),
            RememberTheMilkColorPalette.onPrimary.toArgb(),
            RememberTheMilkColorPalette.surface.toArgb(),
            RememberTheMilkColorPalette.onSurface.toArgb(),
        )
    }

    override fun renderTile(
        state: TileData,
        deviceParameters: DeviceParameters,
    ): LayoutElement {
        return PrimaryLayout.Builder(deviceParameters)
            .setContent(bodyLayout(state, deviceParameters))
            .setPrimaryChipContent(actionChip(deviceParameters))
            .build()
    }

    fun actionChip(
        deviceParameters: DeviceParameters,
    ): LayoutElement =
        CompactChip.Builder(
            context,
            "Open",
            activityClickable(
                context.packageName,
                RememberWearActivity::class.java.name
            ),
            deviceParameters
        ).build()

    fun bodyLayout(
        state: TileData,
        deviceParameters: DeviceParameters
    ) = Column.Builder().apply {
        setHorizontalAlignment(HORIZONTAL_ALIGN_CENTER)
        
        if (state.tasks.isEmpty()) {
            addContent(
                emptyNotice()
            )
        } else {
            state.tasks.forEachIndexed { i, task ->
                if (i > 0) {
                    addContent(Spacer.Builder()
                        .setHeight(DpProp.Builder()
                            .setValue(5f)
                            .build())
                        .build())
                }
                addContent(
                    taskRowChip(task, state.today, deviceParameters)
                )
            }
        }
    }.build()

    fun taskRowChip(
        task: TaskAndTaskSeries,
        today: LocalDate,
        deviceParameters: DeviceParameters
    ): Chip =
        Chip.Builder(context, actionClickable((if (task.isCompleted) "uncomplete:" else "complete:") + task.task.id), deviceParameters)
            .setPrimaryLabelContent(task.taskSeries.name)
            .setIconContent(if (task.isCompleted) "check" else "checkoff")
            .setSecondaryLabelContent(task.task.dueDate.relativeTime(today))
            .setChipColors(ChipColors.primaryChipColors(theme))
            .build()

    fun emptyNotice() = Text.Builder(
        context,
        "No overdue tasks"
    ).apply {
        setMaxLines(3)
        setTypography(Typography.TYPOGRAPHY_DISPLAY1)
        val builder = Modifiers.Builder()
        builder.setSemantics(
            Semantics.Builder().setContentDescription(
                "No overdue tasks"
            ).build()
        )
        setModifiers(
            builder.build()
        )
    }.build()

    fun actionClickable(clickId: String) = Clickable.Builder()
        .setOnClick(
            ActionBuilders.LoadAction.Builder()
                .build()
        )
        .setId(clickId)
        .build()

    fun activityClickable(
        packageName: String,
        activity: String
    ) = Clickable.Builder()
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

    override fun Resources.Builder.produceRequestedResources(
        resourceResults: Unit,
        deviceParameters: DeviceParameters,
        resourceIds: MutableList<String>,
    ) {
        addStatusIcons()
    }

    public fun Resources.Builder.addStatusIcons() {
        addIdToImageMapping(
            "check",
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.baseline_check_box_24)
                        .build()
                ).build()
        )
        addIdToImageMapping(
            "checkoff",
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.baseline_check_box_outline_blank_24)
                        .build()
                ).build()
        )
    }
}

@OptIn(ExperimentalHorologistComposeToolsApi::class)
@WearPreviewDevices
@WearPreviewFontSizes
@Composable
fun NetworkTilePreview() {
    val context = LocalContext.current

    val tileState = SampleData.tileData

    val renderer = remember {
        RememberWearTileRenderer(context)
    }

    TileLayoutPreview(tileState, Unit, renderer)
}
