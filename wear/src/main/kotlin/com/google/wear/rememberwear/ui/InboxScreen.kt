package com.google.wear.rememberwear.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.wear.rememberwear.RememberWearViewModel
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.previews.RememberTheMilkThemePreview

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(),
    onClick: (TaskSeries) -> Unit = {},
    viewModel: RememberWearViewModel
) {
    val paddingHeight = if (LocalConfiguration.current.isScreenRound) 24.dp else 8.dp

    val taskSeries by viewModel.inbox().collectAsState(initial = listOf())

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refetchAllData() },
        refreshTriggerDistance = 16.dp
    ) {
        ScalingLazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                horizontal = 8.dp,
                vertical = paddingHeight,
            ),
            state = scrollState
        ) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(1f),
                    text = "Task List",
                    style = MaterialTheme.typography.title1,
                    textAlign = TextAlign.Center
                )
            }
            items(taskSeries.size) {
                val taskSeries = taskSeries[it]
                TaskSeriesChip(taskSeries = taskSeries, onClick = { onClick(taskSeries) })
            }
        }
    }
}

@Preview(
    widthDp = 300,
    heightDp = 300,
    apiLevel = 26,
    uiMode = Configuration.UI_MODE_TYPE_WATCH
)
@Composable
fun CirclesListPreview() {
    RememberTheMilkThemePreview(round = true) {
    }
}