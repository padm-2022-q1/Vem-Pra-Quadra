package com.reis.vinicius.vempraquadra.data.model

import java.util.*

data class FeedItem (
    val id: UUID,
    val userId: UUID,
    val title: String,
    val content: String,
    val createdAt: Date,
)