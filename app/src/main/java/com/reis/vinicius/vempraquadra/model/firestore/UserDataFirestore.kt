package com.reis.vinicius.vempraquadra.model.firestore

import com.reis.vinicius.vempraquadra.model.entity.UserData
import java.util.*

data class UserDataFirestore (
    val id: String?,
    val name: String?,
    val userName: String?,
    val gender: String?,
    val birth: Date?
){
    fun toEntity() = UserData(
        id = id ?: "",
        name = name ?: "",
        userName = userName ?: "",
        gender = gender ?: "",
        birth = birth ?: Date()
    )

    companion object {
        fun fromEntity (userData: UserData) = UserDataFirestore(
            id = userData.id,
            name = userData.name,
            userName = userData.userName,
            gender = userData.gender,
            birth = userData.birth
        )
    }

    object Fields {
        const val id = "id"
        const val name = "name"
        const val userName = "userName"
        const val gender = "gender"
        const val birth = "birth"
    }
}
