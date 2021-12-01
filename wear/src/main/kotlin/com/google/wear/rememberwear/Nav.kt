package com.google.wear.rememberwear

sealed class Nav(val route: String) {
    object Inbox : Nav("inbox")
    object List : Nav("list")
    object TaskSeries : Nav("taskSeries")
}

