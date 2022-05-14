package com.reis.vinicius.vempraquadra.model.firestore

import com.reis.vinicius.vempraquadra.model.entity.Match
import java.util.*

data class MatchFirestore (
    val name: String? = null,
    val date: Date? = Date(),
    val courtId: Long = 0,
    val usersIds: List<String> = emptyList()
){
    fun toEntity(id: String) = Match(
        id = id ?: "",
        name = name ?: "",
        date = date ?: Date(),
        courtId = courtId,
        usersIds = usersIds
    )

    companion object {
        fun fromEntity(match: Match) = MatchFirestore(
            name = match.name,
            date = match.date,
            courtId = match.courtId,
            usersIds = match.usersIds
        )
    }

    object Fields {
        const val id = "id"
        const val name = "name"
        const val date = "date"
        const val courtId = "courtId"
        const val usersIds = "usersIds"
    }
}
