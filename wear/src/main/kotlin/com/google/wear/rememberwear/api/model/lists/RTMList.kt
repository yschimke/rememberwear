package com.google.wear.rememberwear.api.model.lists

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "list")
data class RTMList(
    @Attribute
    val id: String,

    @Attribute
    val name: String,

    @Attribute
    val deleted: Int,

    @Attribute
    val locked: Int,

    @Attribute
    val archived: Int,

    @Attribute
    val position: Int,

    @Attribute
    val smart: Int,

    @Attribute
    val sort_order: Int,
)
