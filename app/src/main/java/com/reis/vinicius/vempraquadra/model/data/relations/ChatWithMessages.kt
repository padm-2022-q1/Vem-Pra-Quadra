package com.reis.vinicius.vempraquadra.model.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.reis.vinicius.vempraquadra.model.data.entity.Chat
import com.reis.vinicius.vempraquadra.model.data.entity.ChatMessage

data class ChatWithMessages(
    @Embedded val chat: Chat,

    @Relation(
        parentColumn = "id",
        entityColumn = "chatId"
    )
    val messages: List<ChatMessage>
)
