package com.google.wear.rememberwear;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.rememberwear.db.Note
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.Task
import com.google.wear.rememberwear.db.TaskSeries
import com.google.wear.rememberwear.work.ScheduledWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class RememberWearViewModel @Inject constructor(
    private val rememberWearDao: RememberWearDao,
    private val scheduledWork: ScheduledWork
) : ViewModel() {
    val isRefreshing = MutableStateFlow(false)

    fun refetchAllData() {
        viewModelScope.launch {
            isRefreshing.value = true

            try {
                scheduledWork.refetchAllDataWork()
            } finally {
                isRefreshing.value = false
            }
        }
    }

    fun inbox(): Flow<List<TaskSeries>> = rememberWearDao.getAllTaskSeries()

    fun refetchIfStale() {
        viewModelScope.launch {
            val lastRefresh = rememberWearDao.getLastRefresh()

            val stale = lastRefresh == null ||
                    Duration.between(lastRefresh, Instant.now()) > Duration.ofMinutes(10)

            if (stale) {
                refetchAllData()
            }
        }
    }

    fun taskSeries(taskSeriesId: String): Flow<TaskSeries?> =
        rememberWearDao.getTaskSeries(taskSeriesId)

    fun taskSeriesNotes(taskSeriesId: String): Flow<List<Note>> =
        rememberWearDao.getTaskNotes(taskSeriesId)

    fun taskSeriesTasks(taskSeriesId: String): Flow<List<Task>> =
        rememberWearDao.getTasks(taskSeriesId)

    fun uncomplete(taskSeries: TaskSeries) {
    }

    fun complete(taskSeries: TaskSeries) {
    }
}
