package com.reis.vinicius.vempraquadra.model.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.google.firebase.firestore.auth.User
import com.reis.vinicius.vempraquadra.model.data.entity.Match
import com.reis.vinicius.vempraquadra.model.data.junctions.UsersMatches

data class UserWithMatches(
    @Embedded
    val user: User,

    @Relation(
        associateBy = Junction(
            UsersMatches::class,
            parentColumn = "userId",
            entityColumn = "matchId",
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = Match::class,
    )
    val matches: List<Match>
)
