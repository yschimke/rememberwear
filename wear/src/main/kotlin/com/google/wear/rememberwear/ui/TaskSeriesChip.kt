package com.google.wear.rememberwear.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.ToggleChip
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.util.relativeTime

@Composable
fun TaskSeriesChip(
    modifier: Modifier = Modifier,
    taskSeries: TaskSeries,
    onClick: () -> Unit,
) {
    val due = taskSeries.due

    Chip(
        modifier = modifier,
        onClick = onClick,
        label = {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = taskSeries.name, maxLines = 2,
                    color = MaterialTheme.colors.onSurfaceVariant2,
                    style = MaterialTheme.typography.title3,
                )

                if (due != null) {
                    Text(
                        modifier = Modifier.sizeIn(minWidth = 32.dp, maxWidth = 48.dp),
                        text = due.relativeTime(),
                        color = MaterialTheme.colors.secondaryVariant,
                        maxLines = 2,
                        style = MaterialTheme.typography.caption1
                    )
                }
            }
        },
    )
}