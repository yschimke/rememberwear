package com.google.wear.rememberwear.api.model.tasks

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import java.time.Instant
import com.google.wear.rememberwear.db.Note as DBNote

@Xml(name = "note")
data class Note(
    @Attribute val id: String,
    @Attribute val created: Instant,
    @Attribute val modified: Instant,
    @Attribute val title: String,
    @TextContent val body: String,
) {
    fun toDBNote(taskSeriesId: String): DBNote = DBNote(id, taskSeriesId, created, modified, title, body)
}
