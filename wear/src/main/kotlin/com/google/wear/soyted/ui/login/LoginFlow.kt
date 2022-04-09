/*
 * Copyright 2021-2022 Google Inc. All rights reserved.
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

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.Wearable
import com.google.wear.soyted.BuildConfig
import com.google.wear.soyted.app.api.RememberTheMilkService
import com.google.wear.soyted.app.api.model.auth.AuthRsp
import com.google.wear.soyted.app.work.ScheduledWork
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okio.ByteString.Companion.encodeUtf8
import okio.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class LoginFlow @Inject constructor(
    private val authRepository: AuthRepository,
    private val api: RememberTheMilkService,
    @ApplicationContext private val application: Context,
    private val scheduledWork: ScheduledWork,
    private val coroutineScope: CoroutineScope
) {
    suspend fun startLogin(): LoginScreenState.ErrorDetails? {
        val frob = api.frob().frob?.frob

        authRepository.setFrob(frob)

        val remoteActivityHelper =
            RemoteActivityHelper(application, Dispatchers.IO.asExecutor())

        val nodes = Wearable.getNodeClient(application).connectedNodes.await()
        val nodeId = nodes.firstOrNull()?.id

        if (nodeId == null) {
            return LoginScreenState.ErrorDetails("No connected mobile")
        } else {
            try {
                val sig =
                    "${BuildConfig.API_SECRET}api_key${BuildConfig.API_KEY}frob${frob}permsdelete".encodeUtf8()
                        .md5().hex()

                val loginUrl =
                    "https://www.rememberthemilk.com/services/auth/?api_key=${BuildConfig.API_KEY}&perms=delete&frob=$frob&api_sig=$sig"

                remoteActivityHelper.startRemoteActivity(
                    Intent(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(
                            Uri.parse(loginUrl)
                        ),
                    nodeId
                ).await()
            } catch (e: Exception) {
                return LoginScreenState.ErrorDetails("Unable to open mobile app: $e")
            }
        }

        return null
    }

    suspend fun waitForToken(): LoginScreenState.ErrorDetails? {
        val frob = authRepository.getFrob()
            ?: return LoginScreenState.ErrorDetails("Login did not start correctly")

        val auth = waitForAuth(frob)
            ?: return LoginScreenState.ErrorDetails("Invalid token: Did you succesfully authenticate?")

        val token = auth.auth?.token
            ?: return LoginScreenState.ErrorDetails("No token returned by server")

        authRepository.setToken(token)

        coroutineScope.launch {
            scheduledWork.refetchAllDataWork()
        }

        return null
    }

    private suspend fun waitForAuth(
        frob: String
    ): AuthRsp? {
        (1..11).forEach {
            delay(5.seconds)
            try {
                return api.token(frob)
            } catch (ioe: IOException) {
                println(ioe)
            }
        }
        return null
    }
}