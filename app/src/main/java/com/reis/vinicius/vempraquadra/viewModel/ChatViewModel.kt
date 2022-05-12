package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.chat.Chat
import com.reis.vinicius.vempraquadra.model.chat.ChatRepository
import com.reis.vinicius.vempraquadra.model.court.CourtRepository
import com.reis.vinicius.vempraquadra.model.match.Match
import com.reis.vinicius.vempraquadra.model.match.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel(application: Application): MainViewModel<Chat>(application) {
    private val matchRepository = RepositoryFactory(application)
        .create(RepositoryFactory.Object.Chat) as ChatRepository

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(matchRepository.getAll())))
        }
        catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch all objects", e)))
        }
    }
}