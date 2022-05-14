package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.repository.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.repository.ChatRepository

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