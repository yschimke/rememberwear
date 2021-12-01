package com.google.wear.rememberwear.api.model.util;

import java.time.Instant

class InstantTypeConverter : com.tickaroo.tikxml.TypeConverter<Instant?> {
    override fun read(value: String?): Instant? =
        if (value.isNullOrBlank()) {
            null
        } else {
            Instant.parse(value)
        }

    override fun write(value: Instant?): String? = value?.toString()
}
