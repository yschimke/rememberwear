package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml
import java.time.Instant

@Xml(name = "taskseries")
data class TaskSeries(
    @Attribute
    var id: String,

    @Attribute
    var created: Instant,

    @Attribute
    var modified: Instant,

    @Attribute
    var name: String,

    @Attribute
    var source: String,

    @Attribute
    var url: String,

    @Attribute
    var location_id: String,

    @Path("tags")
    @Element
    var tags: List<Tag>?,

//    @Element
//    var participants: MutableList<Task> = mutableListOf(),

    @Path("notes")
    @Element
    var notes: MutableList<Note>?,

    @Element
    var task: List<Task>?,

    @Element
    var rrule: RRule?,
)
