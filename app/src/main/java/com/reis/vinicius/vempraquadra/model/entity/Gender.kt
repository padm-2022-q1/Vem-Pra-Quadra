package com.reis.vinicius.vempraquadra.model.entity

enum class Gender(
    val id: Int,
){
    Male(1),
    Female(2),
    Other(3);

    companion object {
        fun fromInt(id: Int) = values().first { it.id == id }
    }
}