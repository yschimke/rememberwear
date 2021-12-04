package com.google.wear.rememberwear.api.model.tasks

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "tasks", strict = false)
data class Tasks @JvmOverloads constructor(
    @field:Attribute
    var rev: String = "",

    @field:ElementList(inline = true, required = false)
    var list: MutableList<TaskList> = mutableListOf()
)
