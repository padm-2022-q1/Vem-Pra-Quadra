package com.reis.vinicius.vempraquadra.model.feed

import java.util.*

data class Post (
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Date,
    val updatedAt: Date,
    val matchId: String?,
    val createdBy: String,
)