package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ChatMessage (
    @PrimaryKey(autoGenerate = true)
    val id: UUID,
    val message: String,
    val sendDate: Date,
    @Embedded val sentBy: User,
    @Embedded val char: Chat,
)