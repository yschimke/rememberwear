package com.google.wear.rememberwear.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.wear.rememberwear.R
import com.google.wear.rememberwear.db.Todo
import com.google.wear.rememberwear.previews.Previews
import com.google.wear.rememberwear.previews.RememberTheMilkThemePreview

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    scrollState: ScalingLazyListState = rememberScalingLazyListState(),
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onClick: (Todo) -> Unit = {},
) {
    val paddingHeight = if (LocalConfiguration.current.isScreenRound) 12.dp else 8.dp

    val todos = listOf<Todo>()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        refreshTriggerDistance = 16.dp
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(
                horizontal = 8.dp,
                vertical = paddingHeight,
            ),
            state = scrollState
        ) {
            items(todos.size) {
                val todo = todos[it]
                TodoChip(todo = todo)
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