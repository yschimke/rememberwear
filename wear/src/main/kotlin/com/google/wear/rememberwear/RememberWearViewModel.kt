package com.google.wear.rememberwear;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.Todo
import com.google.wear.rememberwear.work.ScheduledWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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

    fun inbox(): Flow<List<Todo>> = rememberWearDao.getAllTodos()

    fun refetchIfStale() {
        viewModelScope.launch {
//            val lastRefresh = rememberWearDao.getLastRefresh()
//
//            val stale = lastRefresh == null ||
//                    Duration.between(lastRefresh, Instant.now()) > Duration.ofMinutes(10)
//
//            if (stale) {
//                refetchAllData()
//            }
        }
    }
}
