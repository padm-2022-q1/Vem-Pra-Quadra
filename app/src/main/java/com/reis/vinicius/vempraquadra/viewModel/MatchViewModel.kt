package com.reis.vinicius.vempraquadra.viewModel

import android.app.Application
import androidx.lifecycle.liveData
import com.reis.vinicius.vempraquadra.model.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.court.CourtRepository
import com.reis.vinicius.vempraquadra.model.match.Match
import com.reis.vinicius.vempraquadra.model.match.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*

class MatchViewModel(application: Application): MainViewModel<Match>(application) {
    private val matchRepository = RepositoryFactory(application)
        .create(RepositoryFactory.Object.Match) as MatchRepository
    private val courtRepository = RepositoryFactory(application)
        .create(RepositoryFactory.Object.Court) as CourtRepository
    private val _name = MutableStateFlow("")
    private val _date = MutableStateFlow("")
    private val _courtId = MutableStateFlow(0L)

    val isSubmitEnabled: Flow<Boolean> = combine(_name, _date, _courtId) { name, date, courtId ->
        val isNameFilled = name.isNotEmpty()
        val isDateFilled = date.isNotEmpty()
        val formattedDate = if (date.isNotEmpty()) SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date) else null
        val isFutureDate = formattedDate != null && formattedDate > Date()
        val isCourtSelected = courtId > 0L

        return@combine isNameFilled and isDateFilled and isFutureDate and isCourtSelected
    }

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(matchRepository.getAll())))
        }
        catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fetch all objects", e)))
        }
    }

    fun insert(match: Match) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(matchRepository.insert(match))))
            _shouldRefresh.postValue(true)
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to add element", e)))
        }
    }

    fun getAllCourts() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Data(courtRepository.getAll())))
        } catch (e: Exception){
            emit(Status.Failure(Exception("Failed to fecth all court items", e)))
        }
    }

    fun setName(name: String) { _name.value = name }
    fun setDate(username: String) { _date.value = username }
    fun setCourt(courtId: Long) { _courtId.value = courtId }
}