package com.google.wear.rememberwear.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.rememberwear.Nav
import com.google.wear.rememberwear.RememberWearViewModel

val uri = "https://www.rememberthemilk.com/"

@OptIn(
    ExperimentalAnimationApi::class,
    androidx.wear.compose.material.ExperimentalWearMaterialApi::class
)
@Composable
fun CircleAppScreens(viewModel: RememberWearViewModel) {
    RememberTheMilkTheme {
        val navController = rememberSwipeDismissableNavController()

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Nav.Inbox.route
        ) {
            composable(
                Nav.Inbox.route,
                deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/" })
            ) {
                InboxScreen()
            }

            composable(
                Nav.Todo.route + "/{todoId}", arguments = listOf(
                    navArgument("todoId", builder = {
                        this.type = NavType.StringType
                    })
                ), deepLinks = listOf(navDeepLink { uriPattern = "$uri/app/#all/{todoId}" })
            ) {
            }
        }
    }

}