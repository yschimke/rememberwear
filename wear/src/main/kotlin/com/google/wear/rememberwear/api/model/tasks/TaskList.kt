package com.google.wear.rememberwear.api.model.tasks

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "list", strict = false)
data class TaskList @JvmOverloads constructor(
    @field:Attribute
    var id: String = "",

    @field:ElementList(inline = true, required = false)
    var taskseries: MutableList<TaskSeries> = mutableListOf()
)
