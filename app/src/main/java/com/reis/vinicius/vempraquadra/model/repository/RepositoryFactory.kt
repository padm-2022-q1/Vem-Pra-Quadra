package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application

class RepositoryFactory(private val application: Application) {
    fun create(obj: Repository.Type): Any = when (obj) {
        Repository.Type.UserData -> UserDataRepository(application)
        Repository.Type.Court -> CourtRepository(application)
        Repository.Type.Match -> MatchRepository(application)
        Repository.Type.Chat -> ChatRepository(application)
    }
}