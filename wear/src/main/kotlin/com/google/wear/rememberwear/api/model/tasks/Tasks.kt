package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "tasks")
data class Tasks(
    @Attribute
    var rev: String,

    @Element
    var list: List<TaskList>?
) {
    val taskSeries: List<TaskSeries>
        get() = list?.flatMap { it.taskseries.orEmpty() }.orEmpty()
}
