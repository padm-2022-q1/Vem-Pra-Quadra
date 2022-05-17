package com.reis.vinicius.vempraquadra.model.entity

import java.util.*

data class Message (
    val id: String,
    val content: String,
    val sentIn: Date?,
    val sentById: String,
    val chatId: String,
    val readByIds: List<String>,
)