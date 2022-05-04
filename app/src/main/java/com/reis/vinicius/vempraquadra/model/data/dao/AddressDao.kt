package com.reis.vinicius.vempraquadra.model.data.dao

import androidx.room.*
import com.reis.vinicius.vempraquadra.model.data.entity.Address

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: Address): Long

    @Update
    fun update(obj: Address): Long

    @Delete
    fun delete(obj: Address)

    @Query("SELECT * FROM address")
    fun getAll(): List<Address>

    @Query("SELECT * FROM address WHERE id = :id")
    fun getById(id: Long): Address?
}