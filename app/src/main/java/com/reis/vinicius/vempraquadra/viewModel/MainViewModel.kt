package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.data.repository.RepositoryFactory
import java.lang.Exception

abstract class MainViewModel(application: Application): AndroidViewModel(application) {
    protected val _shouldRefresh = MutableLiveData(true)

    sealed class Status {
        class Failure(val e: Exception): Status()
        class Success(val result: Result): Status()
        object Loading: Status()
    }

    sealed class Result {
        class Data<T>(val obj: T): Result()
    }
}