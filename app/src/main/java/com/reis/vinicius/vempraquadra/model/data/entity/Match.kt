package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Match (
    @PrimaryKey(autoGenerate = true)
    val id: UUID,
    val name: String,
    val date: Date,
    @Embedded val court: Court,
    @Embedded val chat: Chat,
)