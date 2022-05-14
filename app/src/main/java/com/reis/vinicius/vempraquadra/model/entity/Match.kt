package com.reis.vinicius.vempraquadra.model.entity

import java.util.*

data class Match (
    val id: String,
    val name: String,
    val date: Date,
    val courtId: Long,
    val usersIds: List<String>,
)