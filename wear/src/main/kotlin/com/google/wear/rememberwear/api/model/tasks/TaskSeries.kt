package com.google.wear.rememberwear.api.model.tasks

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "taskseries", strict = false)
data class TaskSeries @JvmOverloads constructor(
    @field:Attribute
    var id: String = "",

    @field:Attribute
    var created: String = "",

    @field:Attribute
    var modified: String = "",

    @field:Attribute
    var name: String = "",

    @field:Attribute
    var source: String = "",

    @field:Attribute
    var url: String = "",

    @field:Attribute
    var location_id: String = "",

//    @field:Element
//    var tags: Object = mutableListOf(),
//
//    @field:Element
//    var participants: MutableList<Task> = mutableListOf(),
//
//    @field:Element
//    var notes: MutableList<Task> = mutableListOf(),

    @field:ElementList(inline = true, required = false)
    var task: MutableList<Task> = mutableListOf()
)
