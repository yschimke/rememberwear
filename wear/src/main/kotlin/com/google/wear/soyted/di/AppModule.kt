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

package com.google.wear.soyted.di

import android.content.Context
import androidx.work.WorkManager
import com.google.wear.soyted.api.RememberTheMilkService
import com.google.wear.soyted.db.RememberWearDao
import com.google.wear.soyted.db.RememberWearDatabase
import com.google.wear.soyted.ui.coilImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

// TODO avoid so many singletons

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    @Named(BaseUrl)
    fun baseUrl() = "https://api.rememberthemilk.com/"

    @Singleton
    @Provides
    fun provideOkHttpClient() = okHttpClient()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named(BaseUrl) baseUrl: String): Retrofit =
        retrofit(baseUrl, okHttpClient)

    @Singleton
    @Provides
    fun provideRememberWearService(retrofit: Retrofit): RememberTheMilkService = retrofit.create(RememberTheMilkService::class.java)

    @Singleton
    @Provides
    fun imageLoader(@ApplicationContext application: Context, client: Provider<OkHttpClient>) =
        coilImageLoader(application, client)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext application: Context): RememberWearDatabase {
        return RememberWearDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideRememberWearDao(rememberWearDatabase: RememberWearDatabase): RememberWearDao {
        return rememberWearDatabase.rememberWearDao()
    }

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext application: Context): WorkManager {
        return WorkManager.getInstance(application)
    }

    const val BaseUrl = "baseurl"
}