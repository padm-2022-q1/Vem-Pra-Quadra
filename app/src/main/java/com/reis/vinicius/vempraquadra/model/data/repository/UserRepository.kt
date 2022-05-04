package com.reis.vinicius.vempraquadra.model.data.repository

import android.app.Application
import androidx.room.Room
import com.reis.vinicius.vempraquadra.model.data.entity.User
import com.reis.vinicius.vempraquadra.model.room.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UserRepository(application: Application): Repository<User> {
    private val db: LocalDatabase by lazy {
        Room.databaseBuilder(application, LocalDatabase::class.java, "users").build()
    }

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        db.userDao().getAll()
    }

    override suspend fun getFirstOrDefault(predicate: (User) -> Boolean): User?  =
        withContext(Dispatchers.IO) {
        db.userDao().getAll().firstOrNull(predicate)
    }

    override suspend fun insert(obj: User): UUID = withContext(Dispatchers.IO) {
        db.userDao().insert(obj)
    }

    override suspend fun update(obj: User): UUID = withContext(Dispatchers.IO) {
        db.userDao().update(obj)
    }

    override suspend fun delete(obj: User): Boolean = withContext(Dispatchers.IO){
        db.userDao().delete(obj)
    }

    override suspend fun deleteAll(objects: List<User>): Boolean = withContext(Dispatchers.IO) {
        db.userDao().deleteAll(objects)
    }

}