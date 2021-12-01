package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rsp")
data class TasksRsp(
    @Attribute
    var stat: String,

    @Element
    var tasks: Tasks?
)
