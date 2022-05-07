package com.reis.vinicius.vempraquadra.model.chat

import java.util.*

data class ChatMessage (
    val id: String,
    val message: String,
    val sendDate: Date,
    val sentBy: String,
    val char: Chat,
)