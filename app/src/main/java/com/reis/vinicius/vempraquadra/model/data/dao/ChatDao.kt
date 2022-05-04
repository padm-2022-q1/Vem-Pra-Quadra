package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.Chat
import com.reis.vinicius.vempraquadra.model.data.entity.Post
import java.util.*

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: Chat): UUID

    @Update
    fun update(obj: Chat): UUID

    @Delete
    fun delete(obj: Chat)

    @Query("SELECT * FROM chat")
    fun getAll(): List<Chat>

    @Query("SELECT * FROM chat WHERE id = :id")
    fun getById(id: String): Chat?
}