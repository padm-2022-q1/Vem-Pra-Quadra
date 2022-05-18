package com.reis.vinicius.vempraquadra.model.entity

data class Court (
    val id: Long,
    val name: String,
    val address: String,
){
    override fun toString(): String {
        return name
    }
}