package com.google.wear.rememberwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import coil.ImageLoader
import coil.compose.LocalImageLoader
import com.google.wear.rememberwear.ui.CircleAppScreens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RememberWearActivity : ComponentActivity() {
    private val viewModel by viewModels<RememberWearViewModel>()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.refetchIfStale()

        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides imageLoader
            ) {
                CircleAppScreens(viewModel)
            }
        }
    }
}

