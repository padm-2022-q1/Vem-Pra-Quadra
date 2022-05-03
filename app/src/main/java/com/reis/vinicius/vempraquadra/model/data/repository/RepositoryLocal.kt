package com.reis.vinicius.vempraquadra.model.data.repository

import android.app.Application

class RepositoryLocal<T>(application: Application): Repository<T> {
    override suspend fun getAll(): List<T> {
        TODO("Not yet implemented")
    }

    override suspend fun getFirstOrDefault(predicate: (T) -> Boolean): T? {
        TODO("Not yet implemented")
    }

    override suspend fun insert(obj: T): T {
        TODO("Not yet implemented")
    }

    override suspend fun update(obj: T): T {
        TODO("Not yet implemented")
    }

    override suspend fun delete(obj: T): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(obj: T): Boolean {
        TODO("Not yet implemented")
    }

}