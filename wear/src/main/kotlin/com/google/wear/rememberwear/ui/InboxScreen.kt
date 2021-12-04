package com.google.wear.rememberwear.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
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
    val paddingHeight = if (LocalConfiguration.current.isScreenRound) 12.dp else 8.dp

    val todos by viewModel.inbox().collectAsState(initial = listOf())

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
            items(todos.size) {
                val todo = todos[it]
                TodoChip(taskSeries = todo)
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