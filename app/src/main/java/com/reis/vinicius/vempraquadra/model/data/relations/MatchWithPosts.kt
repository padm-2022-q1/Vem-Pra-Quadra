package com.reis.vinicius.vempraquadra.model.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.reis.vinicius.vempraquadra.model.data.entity.Match
import com.reis.vinicius.vempraquadra.model.data.entity.Post

data class MatchWithPosts(
    @Embedded val match: Match,

    @Relation(
        parentColumn = "id",
        entityColumn = "matchId",
    )
    val posts: List<Post>
)
