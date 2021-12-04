package com.google.wear.rememberwear.api.model.tasks

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rsp", strict = false)
data class TasksRsp @JvmOverloads constructor(
    @field:Attribute(required = false)
    var stat: String = "",

    @field:Element
    var tasks: Tasks = Tasks()
)
