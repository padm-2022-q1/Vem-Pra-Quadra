package com.reis.vinicius.vempraquadra.model.match

import java.util.*

data class MatchFirestore (
    val name: String? = null,
    val date: Date? = Date(),
    val courtId: Long? = 0,
){
    fun toEntity(id: String) = Match(
        id = id ?: "",
        name = name ?: "",
        date = date ?: Date(),
        courtId = courtId ?: 0,
    )

    companion object {
        fun fromEntity(match: Match) = MatchFirestore(
            name = match.name,
            date = match.date,
            courtId = match.courtId,
        )
    }

    object Fields {
        const val id = "id"
        const val name = "name"
        const val date = "date"
        const val courtId = "courtId"
    }
}
