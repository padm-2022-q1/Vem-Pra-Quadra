package com.reis.vinicius.vempraquadra.model.court

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
        const val CollectionName = "courts"

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
