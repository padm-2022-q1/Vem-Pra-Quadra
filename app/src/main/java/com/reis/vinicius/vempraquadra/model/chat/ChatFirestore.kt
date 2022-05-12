package com.reis.vinicius.vempraquadra.model.chat

import java.util.*

data class ChatFirestore (
    val matchId: String?,
){
    fun toEntity(id: String) = Chat(
        id = id,
        match = null,
    )

    companion object {
        fun fromEntity(chat: Chat) = ChatFirestore(
            matchId = chat.match?.id
        )
    }

    object Fields {
        const val matchId = "matchId"
    }
}
