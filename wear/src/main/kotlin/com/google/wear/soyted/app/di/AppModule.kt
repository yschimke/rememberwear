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

package com.google.wear.soyted.app.di

import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.wear.soyted.app.api.AuthedService
import com.google.wear.soyted.app.api.RememberTheMilkService
import com.google.wear.soyted.app.db.RememberWearDao
import com.google.wear.soyted.app.db.RememberWearDatabase
import com.google.wear.soyted.horologist.snackbar.SnackbarManager
import com.google.wear.soyted.ui.login.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
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
    fun provideOkHttpClient(authRepository: AuthRepository) = okHttpClient(authRepository)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named(BaseUrl) baseUrl: String): Retrofit =
        retrofit(baseUrl, okHttpClient)

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun providesSnackbarManager(): SnackbarManager {
        return SnackbarManager()
    }

    @Singleton
    @Provides
    fun provideRememberWearService(
        retrofit: Retrofit,
        authRepository: AuthRepository
    ): RememberTheMilkService {
        return AuthedService(
            retrofit.create(RememberTheMilkService::class.java),
            authRepository
        )
    }

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

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    const val BaseUrl = "baseurl"
}