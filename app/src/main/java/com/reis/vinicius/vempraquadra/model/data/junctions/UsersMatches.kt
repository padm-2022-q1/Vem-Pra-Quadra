package com.reis.vinicius.vempraquadra.model.data.junctions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.reis.vinicius.vempraquadra.model.data.entity.Match
import com.reis.vinicius.vempraquadra.model.data.entity.User
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["userId"],
            parentColumns = ["id"],
        ),
        ForeignKey(
            entity = Match::class,
            childColumns = ["matchId"],
            parentColumns = ["id"],
        )
    ]
)
data class UsersMatches(
    @ColumnInfo(index = true)
    val userId: UUID,
    @ColumnInfo(index = true)
    val matchId: UUID,
)
