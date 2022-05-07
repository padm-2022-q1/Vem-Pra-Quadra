package com.reis.vinicius.vempraquadra.model

interface Repository<T> {
    suspend fun getAll(): List<T>

    suspend fun getById(id: String): T?

    suspend fun insert(obj: T): String

    suspend fun update(obj: T)

    suspend fun delete(obj: T)

    suspend fun deleteMany(objects: List<T>?)
}