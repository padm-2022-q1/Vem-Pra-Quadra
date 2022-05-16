package com.reis.vinicius.vempraquadra.model.dto

import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.entity.Message

data class ChatWithMessages(
    val chat: Chat,
    val messages: List<Message>
)