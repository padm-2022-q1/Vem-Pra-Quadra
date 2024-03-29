package com.reis.vinicius.vempraquadra.model.firestore

import com.reis.vinicius.vempraquadra.model.entity.Court

data class CourtFirestore (
    val id: Long = 0,
    val name: String? = null,
    val address: String? = null
){
    fun toEntity() = Court(
        id = id,
        name = name ?: "",
        address = address ?: ""
    )

    companion object {
        fun fromEntity(court: Court) = CourtFirestore(
            id = court.id,
            name = court.name,
            address = court.address
        )
    }

    object Fields {
        const val id = "id"
        const val name = "name"
        const val address = "address"
    }
}
