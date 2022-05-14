package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.repository.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.entity.Court
import com.reis.vinicius.vempraquadra.model.repository.CourtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class CourtViewModel(application: Application): MainViewModel<Court>(application) {
    private val repository = RepositoryFactory(application)
        .create(RepositoryFactory.Object.Court) as CourtRepository
    private val _name = MutableStateFlow("")
    private val _address = MutableStateFlow("")

    val isSubmitEnabled: Flow<Boolean> = combine(_name, _address) { name, address ->
        val isNameFilled = name.isNotEmpty()
        val isAddressFilled = address.isNotEmpty()

        return@combine isNameFilled and isAddressFilled
    }

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(repository.getAll())))
        }
        catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch all courts", e)))
        }
    }

    fun insert(obj: Court) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(repository.insert(obj))))
            _shouldRefresh.postValue(true)
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to add element", e)))
        }
    }

    fun setName(name: String) { _name.value = name }
    fun setAddress(username: String) { _address.value = username }
}