package com.reis.vinicius.vempraquadra.model

import android.app.Application
import com.reis.vinicius.vempraquadra.model.chat.ChatRepository
import com.reis.vinicius.vempraquadra.model.court.CourtRepository
import com.reis.vinicius.vempraquadra.model.match.Match
import com.reis.vinicius.vempraquadra.model.match.MatchRepository
import com.reis.vinicius.vempraquadra.model.user.UserDataRepository

class RepositoryFactory(private val application: Application) {
    enum class Object {
        User,
        Court,
        Match,
        Chat,
    }

    object Collections {
        const val Court = "courts"
        const val Match = "matches"
        const val Chat = "chats"
    }

    fun create(obj: Object): Any = when (obj) {
        Object.User -> UserDataRepository(application)
        Object.Court -> CourtRepository(application)
        Object.Match -> MatchRepository(application)
        Object.Chat -> ChatRepository(application)
    }
}