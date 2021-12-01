package com.google.wear.rememberwear.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.util.DebugLogger
import com.google.wear.rememberwear.BuildConfig
import com.google.wear.rememberwear.R
import okhttp3.OkHttpClient
import javax.inject.Provider

fun coilImageLoader(
    application: Context,
    client: Provider<OkHttpClient>
) = ImageLoader.Builder(application)
    .crossfade(true)
    .okHttpClient(client::get)
    .apply {
        if (BuildConfig.DEBUG) {
            logger(DebugLogger(Log.VERBOSE))
        }
    }
    .build()
