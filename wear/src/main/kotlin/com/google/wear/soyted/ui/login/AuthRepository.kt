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

package com.google.wear.soyted.ui.login

import com.google.wear.soyted.app.db.Auth
import com.google.wear.soyted.app.db.RememberWearDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    val dao: RememberWearDao,
    val coroutineScope: CoroutineScope
) {
    val token = MutableStateFlow<String?>(null)

    init {
        coroutineScope.launch {
            dao.getAuth(1).collect {
                token.value = it?.token
                isLoggedIn.value = it?.token != null
            }
        }
    }

    val isLoggedIn = MutableStateFlow(false)

    fun setToken(tokenString: String?) {
        coroutineScope.launch {
            dao.upsertAuth(Auth(1, tokenString))
        }
    }

    fun setFrob(frob: String?) {
        coroutineScope.launch {
            dao.upsertAuth(Auth(2, frob))
        }
    }

    suspend fun getFrob(): String? {
        return dao.getAuth(2).first()?.token
    }
}