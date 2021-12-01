package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import com.google.wear.rememberwear.db.Tag as DBTag

@Xml(name = "rrule")
data class RRule(
    @Attribute
    val every: Int,

    @TextContent
    val rule: String,
)
