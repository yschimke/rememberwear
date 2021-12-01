package com.google.wear.rememberwear.di

import android.content.Context
import androidx.work.WorkManager
import com.google.wear.rememberwear.api.RememberTheMilkService
import com.google.wear.rememberwear.db.AppDatabase
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.ui.coilImageLoader
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
    fun provideRememberWearService(retrofit: Retrofit) = retrofit.create(RememberTheMilkService::class.java)

    @Singleton
    @Provides
    fun imageLoader(@ApplicationContext application: Context, client: Provider<OkHttpClient>) =
        coilImageLoader(application, client)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext application: Context): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideRememberWearDao(appDatabase: AppDatabase): RememberWearDao {
        return appDatabase.rememberWearDao()
    }

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext application: Context): WorkManager {
        return WorkManager.getInstance(application)
    }

    const val BaseUrl = "baseurl"
}