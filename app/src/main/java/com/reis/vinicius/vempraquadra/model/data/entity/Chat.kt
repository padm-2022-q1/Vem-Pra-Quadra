package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity
data class Chat (
    val id: String,
    @Embedded val match: Match,
)