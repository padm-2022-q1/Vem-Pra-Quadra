package com.reis.vinicius.vempraquadra.model

import android.app.Application
import com.reis.vinicius.vempraquadra.model.court.CourtRepository
import com.reis.vinicius.vempraquadra.model.user.UserDataRepository

class RepositoryFactory(private val application: Application) {
    enum class Object {
        User,
        Court,
    }

    fun create(obj: Object): Any = when (obj) {
        Object.User -> UserDataRepository(application)
        Object.Court -> CourtRepository(application)
    }
}