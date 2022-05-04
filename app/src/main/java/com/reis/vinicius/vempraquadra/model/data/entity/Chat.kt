package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Chat (
    @PrimaryKey(autoGenerate = true)
    val id: UUID,
    @Embedded val match: Match,
)