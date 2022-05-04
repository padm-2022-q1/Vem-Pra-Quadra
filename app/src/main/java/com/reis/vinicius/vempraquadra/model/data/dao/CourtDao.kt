package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.Chat
import com.reis.vinicius.vempraquadra.model.data.entity.ChatMessage
import com.reis.vinicius.vempraquadra.model.data.entity.Court
import com.reis.vinicius.vempraquadra.model.data.entity.Post
import java.util.*

@Dao
interface CourtDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: Court): UUID

    @Update
    fun update(obj: Court): UUID

    @Delete
    fun delete(obj: Court)

    @Query("SELECT * FROM court")
    fun getAll(): List<Court>

    @Query("SELECT * FROM court WHERE id = :id")
    fun getById(id: String): Court?
}