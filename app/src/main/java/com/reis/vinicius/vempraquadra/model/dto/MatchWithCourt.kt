package com.reis.vinicius.vempraquadra.model.dto

import com.reis.vinicius.vempraquadra.model.entity.Court
import com.reis.vinicius.vempraquadra.model.entity.Match

data class MatchWithCourt(
    val match: Match,
    val court: Court
)
