package com.google.wear.rememberwear.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.db.TaskSeries.Companion.relativeTime

@Composable
fun TodoChip(
    modifier: Modifier = Modifier,
    taskSeries: TaskSeries,
) {
    TitleCard(
        modifier = modifier,
        onClick = {

        },
        title = {
            Text(taskSeries.name)
        },
        time = {
            Text(taskSeries.due.relativeTime())
        }
    ) {
    }
}