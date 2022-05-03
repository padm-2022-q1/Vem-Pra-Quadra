package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.User
import java.util.*

interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: User): UUID

    @Update
    fun update(obj: User): UUID

    @Delete
    fun delete(obj: User)

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: String): User?
}