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

package com.google.wear.soyted.login

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext val application: Context
) {
    val keyAlias by lazy {
        MasterKey.Builder(application).setKeyScheme(AES256_GCM).build()
    }

    val authPrefs by lazy {
        EncryptedSharedPreferences.create(
            application,
            "soundcloudauthkeys",
            keyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    init {
        authPrefs.registerOnSharedPreferenceChangeListener { _, _ ->
            val tokenString = authPrefs.getString("token", null)
            isLoggedIn.value = tokenString != null
            token.value = tokenString
        }
    }

    val token = MutableStateFlow(authPrefs.getString("token", null))

    val isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(token.value != null)

    fun setToken(tokenString: String?) {
        authPrefs.edit {
            putString("token", tokenString)
        }
    }
}