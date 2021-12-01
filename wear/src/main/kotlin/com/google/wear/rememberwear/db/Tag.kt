package com.google.wear.rememberwear.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey val name: String
)