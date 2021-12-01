package com.google.wear.rememberwear.api.model.tags

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import com.google.wear.rememberwear.db.Tag as DBTag

@Xml(name = "tag")
data class Tag(
    @Attribute
    val name: String,
) {
    fun toDBTag(): DBTag = DBTag(name)
}
