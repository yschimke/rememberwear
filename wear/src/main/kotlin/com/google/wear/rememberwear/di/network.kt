package com.google.wear.rememberwear.di

import com.google.wear.rememberwear.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.LoggingEventListener
import okio.ByteString.Companion.encodeUtf8
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()
}

fun okHttpClient() = OkHttpClient.Builder()
    .apply {
        if (BuildConfig.DEBUG) {
            eventListenerFactory(LoggingEventListener.Factory())
        }
    }
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
        BuildConfig.API_SECRET + unsignedUrl.queryParameterNames.sorted().map { it + unsignedUrl.queryParameter(it) }
            .joinToString("")

    val signedUrl = unsignedUrl.newBuilder().addQueryParameter("api_sig", sig.encodeUtf8().md5().hex()).build()

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

    println("req: " + isImage + " " + response.code + " " + response.request.url)

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