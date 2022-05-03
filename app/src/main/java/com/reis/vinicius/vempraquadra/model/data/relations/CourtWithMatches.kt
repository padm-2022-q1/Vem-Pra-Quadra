package com.reis.vinicius.vempraquadra.model.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.reis.vinicius.vempraquadra.model.data.entity.Court
import com.reis.vinicius.vempraquadra.model.data.entity.Match

data class CourtWithMatches (
    @Embedded val court: Court,

    @Relation(
        parentColumn = "id",
        entityColumn = "courtId"
    )
    val matches: List<Match>
)