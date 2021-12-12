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
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.Wearable
import com.google.wear.soyted.BuildConfig
import com.google.wear.soyted.api.RememberTheMilkService
import com.google.wear.soyted.util.Toaster
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.tasks.await
import okio.ByteString.Companion.encodeUtf8
import javax.inject.Inject

class LoginFlow @Inject constructor(
    val toaster: Toaster,
    val authRepository: AuthRepository,
    val api: RememberTheMilkService,
    @ApplicationContext val application: Context
) {
    suspend fun startLogin() {
        val frob = api.frob().frob?.frob

        authRepository.setFrob(frob)

        val remoteActivityHelper =
            RemoteActivityHelper(application, Dispatchers.IO.asExecutor())

        val nodes = Wearable.getNodeClient(application).connectedNodes.await()
        val nodeId = nodes.firstOrNull()?.id

        if (nodeId == null) {
            Toast.makeText(application, "No connected mobile", Toast.LENGTH_SHORT).show()
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
                toaster.makeToast("Unable to open mobile app: ${e.message}")
            }
        }
    }

    suspend fun enterToken() {
        val frob = authRepository.getFrob()

        if (frob == null) {
            toaster.makeToast("Missing frob")
            return
        }

        val auth = api.token(frob)

        if (auth.err != null) {
            toaster.makeToast("Invalid token: ${auth.err.msg}")
        } else {
            toaster.makeToast("Logged in as ${auth.auth?.user?.fullname}")
        }

        val token = auth.auth?.token

        if (token == null) {
            toaster.makeToast("No token")
        }

        authRepository.setToken(token)
    }
}