package com.reis.vinicius.vempraquadra.model

interface Repository<T> {
    suspend fun getAll(): List<T>

    suspend fun getById(id: String): T?

    suspend fun insert(obj: T)

    suspend fun update(obj: T)

    suspend fun delete(obj: T): Any
}