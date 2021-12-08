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

package com.google.wear.rememberwear.complication

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.wear.complications.data.*
import androidx.wear.complications.datasource.ComplicationDataSourceService
import androidx.wear.complications.datasource.ComplicationDataSourceUpdateRequester
import androidx.wear.complications.datasource.ComplicationRequest
import coil.ImageLoader
import com.google.wear.rememberwear.RememberWearActivity
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.TaskAndTaskSeries
import com.google.wear.rememberwear.util.relativeTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class RememberWearComplicationProviderService : ComplicationDataSourceService() {
    // For coroutines, use a custom scope we can cancel when the service is destroyed
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    @Inject
    lateinit var rememberWearDao: RememberWearDao

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onDestroy() {
        super.onDestroy()
        // Cleans up the coroutine
        serviceJob.cancel()
    }

    override fun onComplicationActivated(complicationInstanceId: Int, type: ComplicationType) {
        Log.i("RememberWear", "onComplicationActivated $complicationInstanceId")
    }

    override fun onComplicationDeactivated(complicationInstanceId: Int) {
        Log.i("RememberWear", "onComplicationDeactivated $complicationInstanceId")
    }

    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener
    ) {
        serviceScope.launch {
            listener.onComplicationData(onComplicationUpdate(request))
        }
    }

    suspend fun onComplicationUpdate(complicationRequest: ComplicationRequest): ComplicationData {
        Log.i("RememberWear", "onComplicationUpdate $complicationRequest")

        val today = LocalDate.now()

        val todos = rememberWearDao.getAllTaskAndTaskSeries().map {
            it.filter { it.isUrgentUncompleted(today) }
        }

        return toComplicationData(complicationRequest.complicationType, todos.first(), today)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return toComplicationData(type, listOf(), LocalDate.now())
    }

    fun toComplicationData(
        type: ComplicationType,
        overdue: List<TaskAndTaskSeries>,
        today: LocalDate
    ): ComplicationData {
        val firstTodo = overdue.firstOrNull()

        return when (type) {
            ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(
                getAddressDescriptionText(firstTodo),
                getAddressDescriptionText(firstTodo)
            )
                .setTitle(getTimeAgoComplicationText(firstTodo?.task?.dueDate, today))
                .setTapAction(applicationContext.tapAction())
                .build()
            else -> throw IllegalArgumentException("Unexpected complication type $type")
        }
    }

    private fun getTimeAgoComplicationText(fromTime: LocalDate?, today: LocalDate): ComplicationText {
        return if (fromTime != null) {
            PlainComplicationText.Builder(fromTime.relativeTime(today)).build()
        } else {
            PlainComplicationText.Builder("No Due Tasks").build()
        }
    }

    private fun getAddressDescriptionText(task: TaskAndTaskSeries?): ComplicationText {
        if (task?.taskSeries != null) {
            return PlainComplicationText.Builder(task.taskSeries.name).build()
        } else {
            return PlainComplicationText.Builder("Up to date").build()
        }
    }

    companion object {
        fun forceComplicationUpdate(applicationContext: Context) {
            val request = ComplicationDataSourceUpdateRequester.create(
                applicationContext, ComponentName(
                    applicationContext, RememberWearComplicationProviderService::class.java
                )
            )
            request.requestUpdateAll()
        }

        fun Context.tapAction(): PendingIntent? {
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.rememberthemilk.com/app/".toUri(),
                this,
                RememberWearActivity::class.java
            )

            return TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }
}