package com.reis.vinicius.vempraquadra.model.entity

import java.util.*

data class ChatMessage (
    val id: String,
    val content: String,
    val sentIn: Date,
    val sentById: String,
)