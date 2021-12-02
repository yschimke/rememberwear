package com.google.wear.rememberwear.api.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "list", strict = false)
data class RTMList @JvmOverloads constructor(
    @field:Attribute
    var id: String = "",

    @field:Attribute
    var name: String = "",

    @field:Attribute
    var deleted: Int = 0,

    @field:Attribute
    var locked: Int = 0,

    @field:Attribute
    var archived: Int = 0,

    @field:Attribute
    var position: Int = 0,

    @field:Attribute
    var smart: Int = 0,

    @field:Attribute
    var sort_order: Int = 0,
)
