package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Post (
    @PrimaryKey(autoGenerate = true)
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: Date,
    val updatedAt: Date,
    @Embedded val match: Match?,
    @Embedded val createdBy: User,
)