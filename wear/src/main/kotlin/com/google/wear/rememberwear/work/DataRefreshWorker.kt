package com.google.wear.rememberwear.work;

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import com.google.wear.rememberwear.api.RememberTheMilkService
import com.google.wear.rememberwear.complication.RememberWearComplicationProviderService
import com.google.wear.rememberwear.db.RememberWearDatabase
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.tile.RememberWearTileProviderService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.Instant

@HiltWorker
class DataRefreshWorker
@AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val rememberWearDatabase: RememberWearDatabase,
    val rememberWearDao: RememberWearDao,
    val rememberTheMilkService: RememberTheMilkService
) : RemoteCoroutineWorker(appContext, workerParams) {
    override suspend fun doRemoteWork(): Result {
        refreshDatabase(rememberWearDatabase, rememberWearDao, rememberTheMilkService)

        RememberWearTileProviderService.forceTileUpdate(applicationContext)
        RememberWearComplicationProviderService.forceComplicationUpdate(applicationContext)

        return Result.success()
    }

    suspend fun refreshDatabase(
        rememberWearDatabase: RememberWearDatabase,
        rememberWearDao: RememberWearDao,
        rememberTheMilkService: RememberTheMilkService
    ) = withContext(Dispatchers.Default) {
        val todosResponse = async { rememberTheMilkService.tasks() }

        val response = todosResponse.await()

        rememberWearDatabase.withTransaction {
            rememberWearDao.deleteAllTodos()

            val tasks = response.tasks.list.flatMap { it.taskseries }

            tasks.forEach {
                rememberWearDao.upsertTodo(TaskSeries(it.id, it.name, Instant.now()))
            }
        }
    }
}
