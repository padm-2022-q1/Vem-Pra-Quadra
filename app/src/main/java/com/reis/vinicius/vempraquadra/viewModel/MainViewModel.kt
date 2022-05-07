package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

abstract class MainViewModel<T>(application: Application): AndroidViewModel(application) {
    protected val _shouldRefresh = MutableLiveData(true)

    sealed class Status {
        class Failure(val e: Exception): Status()
        class Success(val result: Result): Status()
        object Loading: Status()
    }

    sealed class Result {
        data class Data<T>(val obj: T): Result()
    }

    abstract fun getAll(): LiveData<Status>
    abstract fun insert(obj: T): LiveData<Status>
}