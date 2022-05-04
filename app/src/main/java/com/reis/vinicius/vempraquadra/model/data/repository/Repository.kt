package com.reis.vinicius.vempraquadra.model.data.repository

interface Repository<T> {
    suspend fun getAll(): List<T>

    suspend fun getFirstOrDefault(predicate: (T) -> Boolean): T?

    suspend fun insert(obj: T): Any

    suspend fun update(obj: T): Any

    suspend fun delete(obj: T): Boolean

    suspend fun deleteAll(objects: List<T>): Boolean
}