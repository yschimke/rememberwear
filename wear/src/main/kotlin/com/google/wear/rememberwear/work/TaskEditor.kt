package com.google.wear.rememberwear.work

import com.google.wear.rememberwear.api.RememberTheMilkService
import com.google.wear.rememberwear.db.RememberWearDao
import com.google.wear.rememberwear.db.Task
import com.google.wear.rememberwear.db.TaskSeries
import java.time.Instant
import javax.inject.Inject

class TaskEditor @Inject constructor(
    val service: RememberTheMilkService,
    val dao: RememberWearDao
) {
    suspend fun uncomplete(taskSeries: TaskSeries, task: Task) {
        val timeline = service.timeline().timeline.timeline

        service.uncomplete(timeline, taskSeries.listId, taskSeries.id, task.id)
        dao.upsertTask(task.copy(completed = null))
    }

    suspend fun complete(taskSeries: TaskSeries, task: Task) {
        val timeline = service.timeline().timeline.timeline
        service.complete(timeline, taskSeries.listId, taskSeries.id, task.id)

        dao.upsertTask(task.copy(completed = Instant.now()))
    }
}