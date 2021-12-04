package com.google.wear.rememberwear.api.model.tasks

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "list", strict = false)
data class Task @JvmOverloads constructor(
    @field:Attribute
    var id: String = "",

    @field:Attribute
    var due: String = "",

    @field:Attribute
    var has_due_time: Int = 0,

    @field:Attribute
    var added: String = "",

    @field:Attribute
    var completed: String = "",

    @field:Attribute
    var deleted: String = "",

    @field:Attribute
    var priority: String = "",

    @field:Attribute
    var postponed: String = "",

    @field:Attribute
    var estimate: String = "",
)
