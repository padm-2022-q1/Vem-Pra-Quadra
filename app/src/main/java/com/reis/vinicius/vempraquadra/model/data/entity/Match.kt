package com.reis.vinicius.vempraquadra.model.data.entity

import java.util.*

data class Match (
    val id: String,
    val name: String,
    val date: Date,
    val court: Court?,
    val chat: Chat?,
)