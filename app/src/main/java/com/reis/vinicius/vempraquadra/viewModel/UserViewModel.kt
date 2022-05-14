package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.repository.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.entity.UserData
import com.reis.vinicius.vempraquadra.model.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.regex.Pattern

class UserViewModel(application: Application): MainViewModel<UserData>(application) {
    private val repository = RepositoryFactory(application)
        .create(RepositoryFactory.Object.User) as UserDataRepository
    private val _name = MutableStateFlow("")
    private val _username = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _passConfirm = MutableStateFlow("")

    val isSubmitEnabled: Flow<Boolean> = combine(_name, _username, _email, _password,
        _passConfirm) { name, username, email, password, passConfirm ->
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\\\S+\$).{4,}\$"
        val passwordPattern = Pattern.compile(passwordRegex)

        val isNameFilled = name.isNotEmpty()
        val isUserNameFilled = username.isNotEmpty()
        val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty()
        val isPasswordConfirmed = isPasswordValid && password == passConfirm

        return@combine isNameFilled and isUserNameFilled and isEmailValid and isPasswordConfirmed
    }

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(repository.getAll())))
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch all users", e)))
        }
    }

    fun insert(obj: UserData) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(repository.insert(obj))))
            _shouldRefresh.postValue(true)
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to add element", e)))
        }
    }

    fun setName(name: String) { _name.value = name }
    fun setUsername(username: String) { _username.value = username }
    fun setEmail(email: String) { _email.value = email }
    fun setPassword(password: String) { _password.value = password }
    fun setPassConfirm(passConfirm: String) { _passConfirm.value = passConfirm }
}