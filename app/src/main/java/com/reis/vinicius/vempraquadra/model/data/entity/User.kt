package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.auth.User
import java.util.*

@Entity(
    indices = [
        Index(value = ["userName"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: UUID,
    val name: String,
    val userName: String,
    val email: String,
    val password: String,
    val gender: String,
    val birth: Date,
)