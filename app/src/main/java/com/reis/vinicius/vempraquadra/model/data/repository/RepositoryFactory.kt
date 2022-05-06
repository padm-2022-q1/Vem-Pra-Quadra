package com.reis.vinicius.vempraquadra.model.data.repository

import android.app.Application

class RepositoryFactory(private val application: Application) {
    enum class Object {
        User,
    }

    fun create(obj: Object) = when (obj) {
        Object.User -> UserDataRepository(application)
    }
}