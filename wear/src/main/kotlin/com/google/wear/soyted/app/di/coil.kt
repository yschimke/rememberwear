/*
 * Copyright 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.soyted.home

import android.content.Context
import android.util.Log
import coil.ImageLoader
import coil.util.DebugLogger
import com.google.wear.soyted.BuildConfig
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
