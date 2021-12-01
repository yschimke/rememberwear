package com.google.wear.rememberwear

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.wear.rememberwear.work.ScheduledWork
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RememberWearApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var scheduledWork: ScheduledWork

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setDefaultProcessName("${applicationContext.packageName}:data")
            .build()

    override fun onCreate() {
        super.onCreate()

        scheduledWork.createPeriodicWorkRequest()
    }
}

