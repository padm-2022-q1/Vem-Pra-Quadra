package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.reis.vinicius.vempraquadra.model.data.relations.MatchWithUsers
import com.reis.vinicius.vempraquadra.model.data.relations.UserWithMatches

@Dao
interface UsersMatchesDao {
    @Transaction
    @Query("SELECT * FROM user")
    fun getAllUsers(): List<UserWithMatches>

    @Transaction
    @Query("SELECT * FROM [match]")
    fun getAllMatches():List<MatchWithUsers>
}