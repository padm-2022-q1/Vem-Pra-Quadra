package com.reis.vinicius.vempraquadra.model.data.firestore

import java.util.*
import com.reis.vinicius.vempraquadra.model.data.entity.Match

data class MatchFirestore (
    val id: String?,
    val name: String?,
    val date: Date?,
    val courtId: Long?,
    val chatId: String?
){
    fun toEntity() = Match(
        id = id ?: "",
        name = name ?: "",
        date = date ?: Date(),
        court = null,
        chat = null,
    )

    companion object {
        const val CollectionName = "matches"

        fun fromEntity(match: Match) = MatchFirestore(
            id = match.id,
            name = match.name,
            date = match.date,
            courtId = match.court?.id,
            chatId = match.chat?.id,
        )
    }

    object Fields {
        const val id = "id"
        const val name = "name"
        const val date = "date"
        const val courtId = "courtId"
        const val chatId = "chatId"
    }
}
