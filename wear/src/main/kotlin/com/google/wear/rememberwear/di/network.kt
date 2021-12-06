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

package com.google.wear.rememberwear.di

import com.google.wear.rememberwear.BuildConfig
import com.google.wear.rememberwear.api.model.util.InstantTypeConverter
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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

fun okHttpClient() = OkHttpClient.Builder()
    .apply {
        if (BuildConfig.DEBUG) {
//            eventListenerFactory(LoggingEventListener.Factory())

//            addInterceptor(HttpLoggingInterceptor().apply {
//                this.level = HttpLoggingInterceptor.Level.BODY
//            })
        }
    }
    .addInterceptor(BrotliInterceptor)
    .addInterceptor(authInterceptor())
    .addNetworkInterceptor(cacheImagesInterceptor())
    .build()

fun authInterceptor() = Interceptor { chain ->
    var request = chain.request()

    val unsignedUrl = request.url.newBuilder()
        .addQueryParameter("api_key", BuildConfig.API_KEY)
        .apply {
            if (BuildConfig.TOKEN != null) {
                addQueryParameter("auth_token", BuildConfig.TOKEN)
            }
        }
        .build()

    val sig =
        BuildConfig.API_SECRET + unsignedUrl.queryParameterNames.sorted()
            .map { it + unsignedUrl.queryParameter(it) }
            .joinToString("")

    val signedUrl =
        unsignedUrl.newBuilder().addQueryParameter("api_sig", sig.encodeUtf8().md5().hex()).build()

    request = request.newBuilder()
        .url(signedUrl)
        .build()

    chain.proceed(request)
}

// Rewrite the Cache-Control header to cache all responses for a week.
// Not all images have consistent cache headers.
fun cacheImagesInterceptor() = Interceptor {
    val response = it.proceed(it.request())

    val isImage = response.body?.contentType()?.type == "image"

    val forceCache = isImage200(response, isImage) || isImage301(response)
    if (forceCache) {
        response.newBuilder()
            .header("Cache-Control", "max-age=604800,public")
            .build()
    } else {
        response
    }
}

private fun isImage200(response: Response, isImage: Boolean) =
    response.code == 200 && isImage

private fun isImage301(response: Response) =
    response.code == 301 && response.request.url.pathSegments.last().endsWith(".jpg")