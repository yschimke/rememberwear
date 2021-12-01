package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import com.google.wear.rememberwear.db.Tag as DBTag

@Xml(name = "tag")
data class Tag(
    @TextContent val name: String,
) {
    fun toDBTag(): DBTag = DBTag(name)
}
