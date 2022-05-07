package com.reis.vinicius.vempraquadra.model.match

import com.reis.vinicius.vempraquadra.model.chat.Chat
import com.reis.vinicius.vempraquadra.model.court.Court
import java.util.*

data class Match (
    val id: String,
    val name: String,
    val date: Date,
    val court: Court?,
    val chat: Chat?,
)