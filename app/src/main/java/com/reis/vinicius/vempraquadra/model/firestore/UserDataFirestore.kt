package com.reis.vinicius.vempraquadra.model.firestore

import com.reis.vinicius.vempraquadra.model.entity.UserData
import java.util.*

data class UserDataFirestore (
    val name: String = "",
    val userName: String = "",
    val genderId: Int = 2,
    val birth: Date = Date()
){
    fun toEntity(id: String) = UserData(
        id = id,
        name = name,
        userName = userName,
        genderId = genderId,
        birth = birth
    )

    companion object {
        fun fromEntity (userData: UserData) = UserDataFirestore(
            name = userData.name,
            userName = userData.userName,
            genderId = userData.genderId,
            birth = userData.birth
        )
    }

    object Fields {
        const val name = "name"
        const val userName = "userName"
        const val genderId = "genderId"
        const val birth = "birth"
    }
}
