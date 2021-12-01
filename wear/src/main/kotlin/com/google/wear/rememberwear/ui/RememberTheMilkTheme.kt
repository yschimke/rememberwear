package com.google.wear.rememberwear.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

private val RememberTheMilkColorPalette = Colors(
    primary = Color(0xff8055f6L),
    primaryVariant = Color(0xffb684ffL),
    onPrimary = Color(0xFFffffffL),
    secondary = Color(0xff85c8dbL),
    secondaryVariant = Color(0xff240047L),
    onSecondary = Color(0xFF4728c2L),
)

@Composable
fun RememberTheMilkTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = RememberTheMilkColorPalette,
        content = content
    )
}

val TimeFormatter =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
val DateFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

fun formatTime(instant: ZonedDateTime) =
    if (instant.plus(24, ChronoUnit.HOURS) > ZonedDateTime.now()) {
        TimeFormatter.format(instant)
    } else {
        DateFormatter.format(instant)
    }

fun formatTime(instant: Instant) =
    formatTime(ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault()))