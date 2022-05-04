package com.reis.vinicius.vempraquadra.model.data.repository

import android.app.Application
import com.reis.vinicius.vempraquadra.model.data.entity.User
import kotlin.Exception

class RepositoryFactory(private val application: Application) {
    enum class Storage {
        Local,
    }

    enum class Object {
        User,
    }

    fun create(storage: Storage, obj: Object) = when (storage){
        Storage.Local -> when (obj) {
            Object.User -> UserRepository(application)
        }
    }

}