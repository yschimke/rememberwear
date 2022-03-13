/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.soyted.horologist.navscaffold

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.google.wear.soyted.horologist.navscaffold.NavScaffoldViewModel.VignetteMode.Off
import com.google.wear.soyted.horologist.navscaffold.NavScaffoldViewModel.VignetteMode.On
import com.google.wear.soyted.horologist.navscaffold.NavScaffoldViewModel.VignetteMode.WhenScrollable
import com.google.wear.soyted.horologist.snackbar.SnackbarManager
import com.google.wear.soyted.horologist.snackbar.material.SnackbarDuration
import com.google.wear.soyted.horologist.snackbar.material.SnackbarHostState
import kotlinx.coroutines.launch

/**
 * A ViewModel that backs the WearNavScaffold to allow each composable to interact and effect
 * the [Scaffold] positionIndicator, vignette and timeText.
 *
 * A ViewModel is used to allow the same current instance to be shared between the WearNavScaffold
 * and the composable screen via [NavHostController.currentBackStackEntry].
 */
open class NavScaffoldViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var initialIndex: Int? = null
    internal var scrollType by mutableStateOf<ScrollType?>(null)

    private lateinit var _scrollableState: ScrollableState

    /**
     * Returns the scrollable state for this composable or null if the scaffold should
     * not consider this element to be scrollable.
     */
    public val scrollableState: ScrollableState?
        get() = if (scrollType == null || scrollType == ScrollType.None) {
            null
        } else {
            _scrollableState
        }

    /**
     * The configuration of [Vignette], [WhenScrollable], [Off], [On] and if so whether top and
     * bottom. Defaults to on for scrollable screens.
     */
    public var vignettePosition: VignetteMode by mutableStateOf(VignetteMode.WhenScrollable)

    /**
     * The configuration of [TimeText], defaults to FadeAway which will move the time text above the
     * screen to avoid overlapping with the content moving up.
     */
    public var timeTextMode: TimeTextMode by mutableStateOf(TimeTextMode.FadeAway)

    /**
     * The configuration of [PositionIndicator].  The default is to show a scroll bar while the
     * scroll is in progress.
     */
    public var positionIndicatorMode: PositionIndicatorMode
            by mutableStateOf(PositionIndicatorMode.On)

    internal var focusRequested: Boolean = false

    /**
     * A [FocusRequester] to be passed into Scrollable composables.
     * If this is accessed within [NavGraphBuilder.composable] then
     */
    public val focusRequester: FocusRequester by lazy {
        focusRequested = true
        FocusRequester()
    }

    internal fun initialiseScrollState(scrollStateBuilder: () -> ScrollState): ScrollState {
        check(scrollType == null || scrollType == ScrollType.ScrollState)

        if (scrollType == null) {
            scrollType = ScrollType.ScrollState

            _scrollableState = savedStateHandle.saveable(
                key = "navScaffold.ScrollState",
                saver = ScrollState.Saver
            ) {
                scrollStateBuilder()
            }
        }

        return _scrollableState as ScrollState
    }

    internal fun initialiseScalingLazyListState(
        scrollableStateBuilder: () -> ScalingLazyListState
    ): ScalingLazyListState {
        check(scrollType == null || scrollType == ScrollType.ScalingLazyColumn)

        if (scrollType == null) {
            scrollType = ScrollType.ScalingLazyColumn

            _scrollableState = savedStateHandle.saveable(
                key = "navScaffold.ScalingLazyListState",
                saver = ScalingLazyListState.Saver
            ) {
                scrollableStateBuilder().also {
                    initialIndex = it.centerItemIndex
                }
            }
        }

        return _scrollableState as ScalingLazyListState
    }

    internal fun initialiseLazyList(
        scrollableStateBuilder: () -> LazyListState
    ): LazyListState {
        check(scrollType == null || scrollType == ScrollType.LazyList)

        if (scrollType == null) {
            scrollType = ScrollType.LazyList

            _scrollableState = savedStateHandle.saveable(
                key = "navScaffold.LazyListState",
                saver = LazyListState.Saver
            ) {
                scrollableStateBuilder()
            }
        }

        return _scrollableState as LazyListState
    }

    internal enum class ScrollType {
        None, ScalingLazyColumn, ScrollState, LazyList
    }

    /**
     * The configuration of [TimeText], defaults to FadeAway which will move the time text above the
     * screen to avoid overlapping with the content moving up.
     */
    public enum class TimeTextMode {
        On, Off, FadeAway
    }

    /**
     * The configuration of [PositionIndicator].  The default is to show a scroll bar while the
     * scroll is in progress.
     */
    public enum class PositionIndicatorMode {
        On, Off
    }

    /**
     * The configuration of [Vignette], [WhenScrollable], [Off], [On] and if so whether top and
     * bottom. Defaults to on for scrollable screens.
     */
    public sealed interface VignetteMode {
        public object WhenScrollable : VignetteMode
        public object Off : VignetteMode
        public data class On(val position: VignettePosition) : VignetteMode
    }
}
