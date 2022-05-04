package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.*
import java.util.*

@Dao
interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: Match): UUID

    @Update
    fun update(obj: Match): UUID

    @Delete
    fun delete(obj: Match)

    @Query("SELECT * FROM [match]")
    fun getAll(): List<Match>

    @Query("SELECT * FROM [match] WHERE id = :id")
    fun getById(id: String): Match?
}