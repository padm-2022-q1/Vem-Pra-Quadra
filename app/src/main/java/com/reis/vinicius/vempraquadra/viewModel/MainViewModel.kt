package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.data.entity.User
import com.reis.vinicius.vempraquadra.model.data.repository.RepositoryFactory
import java.lang.Exception

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val userRepository = RepositoryFactory(application).create(
        RepositoryFactory.Storage.Local, RepositoryFactory.Object.User
    )

    sealed class Status {
        class Failure(val e: Exception): Status()
        class Success(val result: Result): Status()
        object Loading: Status()
    }

    sealed class Result{
        class Data<T>(val obj: T): Result()
    }

    fun insertUser(user: User) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(userRepository.insert(user))))
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to add user", e)))
        }
    }
}