package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.Post
import java.util.*

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: Post): UUID

    @Update
    fun update(obj: Post): UUID

    @Delete
    fun delete(obj: Post)

    @Query("SELECT * FROM post")
    fun getAll(): List<Post>

    @Query("SELECT * FROM post WHERE id = :id")
    fun getById(id: String): Post?
}