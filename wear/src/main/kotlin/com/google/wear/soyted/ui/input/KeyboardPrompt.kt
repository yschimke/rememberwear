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

package com.google.wear.soyted.ui.input

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender

object KeyboardPrompt {
    @Composable
    fun keyboardPromptLauncher(
        onTextEntered: (String) -> Unit,
        onError: (String) -> Unit
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val text: String? = results.getCharSequence("text")?.toString()

                if (text != null) {
                    onTextEntered(text)
                    return@rememberLauncherForActivityResult
                }

                onError("No text, ${results.keySet()}")
            }
        }
    }


    val keyboardPromptIntent = RemoteInputIntentHelper.createActionRemoteInputIntent().apply {
        val remoteInputs: List<RemoteInput> = listOf(
            RemoteInput.Builder("text")
                .setLabel("Text Input")
                .wearableExtender {
                    setEmojisAllowed(false)
                    setInputActionType(EditorInfo.IME_ACTION_DONE)
                }
                .build()
        )

        RemoteInputIntentHelper.putRemoteInputsExtra(this, remoteInputs)
    }
}
