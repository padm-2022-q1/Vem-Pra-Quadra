package com.reis.vinicius.vempraquadra.model.firestore

import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.model.entity.Message

data class ChatFirestore (
    val matchId: String = "",
){
    fun toEntity(
        id: String,
        match: Match?,
        lastMessage: Message?
    ) = Chat(
        id = id,
        match = match,
        lastMessage = lastMessage
    )

    companion object {
        fun fromEntity(chat: Chat) = ChatFirestore(
            matchId = chat.match?.id ?: ""
        )
    }

    object Fields {
        const val matchId = "matchId"
    }
}
