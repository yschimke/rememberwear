package com.google.wear.rememberwear.api.model.tasks

import com.google.wear.rememberwear.db.Task
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import java.time.Instant

@Xml(name = "task")
data class Task(
    @Attribute
    val id: String,

    @Attribute
    val due: Instant?,

    @Attribute
    val has_due_time: Int,

    @Attribute
    val added: Instant,

    @Attribute
    val completed: Instant?,

    @Attribute
    val deleted: Instant?,

    @Attribute
    val priority: String,

    @Attribute
    val postponed: String,

    @Attribute
    val estimate: String,
) {
    fun toDBTask(taskSeriesId: String): Task = Task(
        this.id,
        taskSeriesId,
        this.due,
        this.added,
        this.completed,
        this.deleted,
        this.priority,
        this.postponed,
        this.estimate
    )
}
