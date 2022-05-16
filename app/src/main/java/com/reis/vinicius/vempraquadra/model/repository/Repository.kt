package com.reis.vinicius.vempraquadra.model.repository

interface Repository<T> {
    object Collections {
        const val Court = "courts"
        const val Match = "matches"
        const val Chat = "chats"
        const val UserData = "usersData"
        const val ChatMessage = "messages"
    }

    enum class Type {
        UserData,
        Court,
        Match,
        Chat,
    }

    suspend fun getAll(): List<T>

    suspend fun getById(id: String): T?

    suspend fun insert(obj: T)

    suspend fun update(obj: T)

    suspend fun delete(obj: T): Any
}