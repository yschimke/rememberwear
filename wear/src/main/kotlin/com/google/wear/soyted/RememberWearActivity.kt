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

package com.google.wear.soyted

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import coil.ImageLoader
import coil.compose.LocalImageLoader
import com.google.wear.soyted.ui.RememberWearAppScreens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@AndroidEntryPoint
class RememberWearActivity : ComponentActivity() {
    private val viewModel by viewModels<RememberWearViewModel>()

    @Inject
    lateinit var imageLoader: ImageLoader

    val voicePromptLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Task to remember"
            )
        }
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)

            if (spokenText != null) {
                viewModel.createTask(spokenText)
            }
        } else {
            Toast.makeText(this, "Failed adding toast", Toast.LENGTH_SHORT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.refetchIfStale()

        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides imageLoader
            ) {
                RememberWearAppScreens(viewModel, onVoicePrompt = {
                    voicePromptLauncher.launch()
                })
            }
        }
    }
}

