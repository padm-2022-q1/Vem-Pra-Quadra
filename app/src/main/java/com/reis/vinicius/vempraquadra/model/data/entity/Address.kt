package com.reis.vinicius.vempraquadra.model.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val street: String,
    val hood: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String,
)