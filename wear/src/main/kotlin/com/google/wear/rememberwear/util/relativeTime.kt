package com.google.wear.rememberwear.util

import android.text.format.DateUtils
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import java.time.Instant

fun Instant.relativeTime() = DateUtils.getRelativeTimeSpanString(
    toEpochMilli(),
    System.currentTimeMillis(),
    MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_ALL
).toString()