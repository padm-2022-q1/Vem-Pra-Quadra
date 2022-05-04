package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.Chat
import com.reis.vinicius.vempraquadra.model.data.entity.ChatMessage
import com.reis.vinicius.vempraquadra.model.data.entity.Post
import java.util.*

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: ChatMessage): UUID

    @Update
    fun update(obj: ChatMessage): UUID

    @Delete
    fun delete(obj: ChatMessage)

    @Query("SELECT * FROM chatMessage")
    fun getAll(): List<Chat>

    @Query("SELECT * FROM chatMessage WHERE id = :id")
    fun getById(id: String): ChatMessage?
}