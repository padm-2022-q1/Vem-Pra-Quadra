package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.repository.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.repository.ChatRepository
import com.reis.vinicius.vempraquadra.model.repository.Repository

class ChatViewModel(application: Application): MainViewModel<Chat>(application) {
    private val chatRepository = RepositoryFactory(application)
        .create(Repository.Type.Chat) as ChatRepository

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(chatRepository.getAll())))
        }
        catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch all objects", e)))
        }
    }
}