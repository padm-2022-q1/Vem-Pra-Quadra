package com.reis.vinicius.vempraquadra.model.match

import java.util.*

data class Match (
    val id: String,
    val name: String,
    val date: Date,
    val courtId: Long,
)