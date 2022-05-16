package com.reis.vinicius.vempraquadra.model.firestore

import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.entity.Message
import java.util.*

data class MessageFirestore (
    val content: String = "",
    val sentIn: Date = Date(),
    val sentById: String = "",
    val chatId: String = "",
    val readByIds: List<String> = emptyList(),
){
    fun toEntity(id: String) = Message(
        id = id,
        content = content,
        sentIn = sentIn,
        sentById = sentById,
        chatId = chatId,
        readByIds = readByIds,
    )

    companion object {
        fun fromEntity(message: Message) = MessageFirestore(
            content = message.content,
            sentIn = message.sentIn,
            sentById = message.sentById,
            chatId = message.chatId,
            readByIds = message.readByIds,
        )
    }

    object Fields {
        const val content = "content"
        const val sentIn = "sentIn"
        const val sentById = "sentById"
        const val chatId = "chatId"
        const val readByIds = "readByIds"
    }
}
