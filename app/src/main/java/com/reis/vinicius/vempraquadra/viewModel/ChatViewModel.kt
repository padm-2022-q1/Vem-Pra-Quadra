package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.repository.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.entity.Message
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

    fun getAllByUser(userId: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(chatRepository.getAllByUser(userId))))
        }
        catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch objects from user $userId", e)))
        }
    }

    fun getWithMessagesById(id: String, userId: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(chatRepository.getWithMessagesById(id, userId))))
        }
        catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch object with id $id", e)))
        }
    }

    fun saveMessage(message: Message) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(chatRepository.saveMessage(message))))
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to save message", e)))
        }
    }
}