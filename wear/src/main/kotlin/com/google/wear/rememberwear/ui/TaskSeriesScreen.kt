package com.google.wear.rememberwear.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.ToggleChip
import com.google.wear.rememberwear.RememberWearViewModel
import com.google.wear.rememberwear.util.relativeTime
import java.time.LocalDate

@Composable
fun TaskSeriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RememberWearViewModel,
    taskSeriesId: String
) {
    val taskSeries by viewModel.taskSeries(taskSeriesId).collectAsState(initial = null)
    val notes by viewModel.taskSeriesNotes(taskSeriesId).collectAsState(listOf())
    val tasks by viewModel.taskSeriesTasks(taskSeriesId).collectAsState(listOf())

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        val taskSeries = taskSeries
        if (taskSeries != null) {
            TitleCard(
                modifier = modifier,
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        text = taskSeries.name,
                        maxLines = 2
                    )
                },
                onClick = {}
            ) {
                Column {
                    if (taskSeries.due != null) {
                        val completed = false

                        ToggleChip(checked = completed, onCheckedChange = {
                            if (completed) {
                                viewModel.uncomplete(taskSeries)
                            } else {
                                viewModel.complete(taskSeries)
                            }
                        }, label = {
                            Text(
                                text = taskSeries.due.relativeTime(),
                            )
                        })
                    }
                }

                notes.forEach {
                    Text(it.body)
                }

                if (taskSeries.isRepeating) {
                    tasks.forEach {

                    }
                }
            }
        }
    }
}
