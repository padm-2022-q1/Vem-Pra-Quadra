package com.reis.vinicius.vempraquadra.model.entity

import java.util.*

data class FeedItem (
    val id: UUID,
    val userId: UUID,
    val title: String,
    val content: String,
    val createdAt: Date,
)