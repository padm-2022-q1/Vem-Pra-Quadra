package com.reis.vinicius.vempraquadra.model.dto

import com.firebase.ui.auth.data.model.User
import com.reis.vinicius.vempraquadra.model.entity.Message
import com.reis.vinicius.vempraquadra.model.entity.UserData

data class MessageWithUserData(
    val message: Message,
    val user: UserData
)
