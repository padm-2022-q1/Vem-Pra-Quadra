package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Court (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    @Embedded val address: Address,
)