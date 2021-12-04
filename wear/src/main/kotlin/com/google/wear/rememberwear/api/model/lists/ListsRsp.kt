package com.google.wear.rememberwear.api.model.lists

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rsp", strict = false)
data class ListsRsp @JvmOverloads constructor(
    @field:Attribute(required = false)
    var stat: String = "",

    @field:ElementList
    var lists: MutableList<List> = mutableListOf()
)
