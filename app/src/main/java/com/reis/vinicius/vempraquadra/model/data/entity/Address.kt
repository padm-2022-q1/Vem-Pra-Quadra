package com.reis.vinicius.vempraquadra.model.data.entity

data class Address (
    val id: Long,
    val street: String,
    val hood: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String,
)