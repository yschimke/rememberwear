package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "list")
data class TaskList(
    @Attribute
    var id: String,

    @Element
    var taskseries: List<TaskSeries>?
)
