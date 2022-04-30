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

package com.google.wear.soyted.app.di

import com.google.wear.soyted.BuildConfig
import com.google.wear.soyted.app.api.model.util.InstantTypeConverter
import com.google.wear.soyted.ui.login.AuthRepository
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.brotli.BrotliInterceptor
import okio.ByteString.Companion.encodeUtf8
import retrofit2.Retrofit
import java.time.Instant

fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient
): Retrofit {
    val parser = TikXml.Builder()
        .addTypeConverter(Instant::class.java, InstantTypeConverter())
        .build()

    return Retrofit.Builder()
        .addConverterFactory(TikXmlConverterFactory.create(parser))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()
}

fun okHttpClient(authRepository: AuthRepository) = OkHttpClient.Builder()
    .apply {
        // Allow network inspector in debug builds
        if (!BuildConfig.DEBUG) {
            addInterceptor(BrotliInterceptor)
        }
    }
    .addInterceptor(authInterceptor(authRepository))
    .build()

fun authInterceptor(authRepository: AuthRepository) = Interceptor { chain ->
    var request = chain.request()

    val queryToken = request.url.queryParameter("auth_token")
    val token = queryToken ?: authRepository.token.value

    val method = request.url.queryParameter("method")

    if (method == "rtm.auth.getFrob" || method == "rtm.auth.getToken") {
        val unsignedUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val signedUrl =
            unsignedUrl.newBuilder()
                .addQueryParameter("api_sig", unsignedUrl.signature().encodeUtf8().md5().hex())
                .build()

        request = request.newBuilder()
            .url(signedUrl)
            .build()
    } else {
        if (token == null) {
            println("401: No token")
            return@Interceptor Response.Builder()
                .request(request)
                .code(401)
                .protocol(Protocol.HTTP_2)
                .message("No token")
                .body("".toResponseBody())
                .build()
        }

        val unsignedUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .setQueryParameter("auth_token", token)
            .build()

        val signedUrl =
            unsignedUrl.newBuilder()
                .addQueryParameter("api_sig", unsignedUrl.signature().encodeUtf8().md5().hex())
                .build()

        request = request.newBuilder()
            .url(signedUrl)
            .build()
    }

    val response = chain.proceed(request)

    response
}

fun HttpUrl.signature(): String = BuildConfig.API_SECRET + queryParameterNames.sorted()
    .joinToString("") { it + queryParameter(it) }
