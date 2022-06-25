package com.google.wear.soyted.previews

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Large Round",
)
@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Small Round",
)
@Preview(
    device = Devices.WEAR_OS_SQUARE,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Square",
)
public annotation class WearPreviewDevices

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Small Round",
)
public annotation class WearSmallRoundDevicePreview

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Large Round",
)
public annotation class WearLargeRoundDevicePreview

@Preview(
    device = Devices.WEAR_OS_SQUARE,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Square",
)
public annotation class WearSquareDevicePreview
