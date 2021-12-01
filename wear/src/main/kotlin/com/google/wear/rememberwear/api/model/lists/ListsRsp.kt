package com.google.wear.rememberwear.api.model.lists

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rsp")
data class ListsRsp(
    @Attribute
    val stat: String,

    @Path("lists")
    @Element
    var lists: List<RTMList>
)
