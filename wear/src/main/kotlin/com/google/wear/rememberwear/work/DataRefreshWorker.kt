package com.google.wear.rememberwear.work;

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import com.google.wear.rememberwear.api.RememberTheMilkService
import com.google.wear.rememberwear.complication.RememberWearComplicationProviderService
import com.google.wear.rememberwear.db.AppDatabase
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.tile.RememberWearTileProviderService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

@HiltWorker
class DataRefreshWorker
@AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val appDatabase: AppDatabase,
    val rememberWearDao: RememberWearDao,
    val rememberTheMilkService: RememberTheMilkService
) : RemoteCoroutineWorker(appContext, workerParams) {
    override suspend fun doRemoteWork(): Result {
        refreshDatabase(appDatabase, rememberWearDao, rememberTheMilkService)

        RememberWearTileProviderService.forceTileUpdate(applicationContext)
        RememberWearComplicationProviderService.forceComplicationUpdate(applicationContext)

        return Result.success()
    }

    suspend fun refreshDatabase(
        appDatabase: AppDatabase,
        rememberWearDao: RememberWearDao,
        rememberTheMilkService: RememberTheMilkService
    ) = withContext(Dispatchers.Default) {
        val todosResponse = async { rememberTheMilkService.todos() }

        appDatabase.withTransaction {
            rememberWearDao.deleteAllTodos()

            todosResponse.await().forEach {
                rememberWearDao.upsertTodo(it)
            }
        }
    }
}
