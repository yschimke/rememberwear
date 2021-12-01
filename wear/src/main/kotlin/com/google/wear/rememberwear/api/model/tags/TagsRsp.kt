package com.google.wear.rememberwear.api.model.tags

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rsp")
data class TagsRsp(
    @Attribute
    val stat: String,

    @Path("tags")
    @Element
    var tags: List<Tag>?
)
