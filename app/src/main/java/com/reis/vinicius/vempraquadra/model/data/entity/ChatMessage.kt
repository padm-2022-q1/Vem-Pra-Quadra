package com.reis.vinicius.vempraquadra.model.data.entity

import java.util.*

data class ChatMessage (
    val id: String,
    val message: String,
    val sendDate: Date,
    val sentBy: String,
    val char: Chat,
)