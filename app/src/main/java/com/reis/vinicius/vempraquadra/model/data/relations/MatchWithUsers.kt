package com.reis.vinicius.vempraquadra.model.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.google.firebase.firestore.auth.User
import com.reis.vinicius.vempraquadra.model.data.entity.Match
import com.reis.vinicius.vempraquadra.model.data.junctions.UsersMatches

data class MatchWithUsers(
    @Embedded
    val match: Match,

    @Relation(
        associateBy = Junction(
            UsersMatches::class,
            parentColumn = "matchId",
            entityColumn = "userId",
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = User::class,
    )
    val users: List<User>
)
