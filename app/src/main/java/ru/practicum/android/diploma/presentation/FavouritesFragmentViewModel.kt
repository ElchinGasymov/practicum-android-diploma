package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavouritesFragmentViewModel(
    private val selectedVacanciesInteractor: SelectedVacanciesInteractor
) : ViewModel() {

    private val _listVacancy = MutableLiveData<List<Vacancy>>()
    val listVacancy: LiveData<List<Vacancy>>
        get() = _listVacancy

    fun getVacancyList() {
        viewModelScope.launch {
            selectedVacanciesInteractor.listVacancies().collect {
                _listVacancy.postValue(it)
            }
        }
    }

    fun addVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            selectedVacanciesInteractor.addVacancy(vacancy)
        }
    }

    fun deleteVacancy(vacancyId: Long) {
        viewModelScope.launch {
            selectedVacanciesInteractor.deleteVacancy(vacancyId)
        }
    }

    private val _hasLikeAnswer = MutableLiveData<Boolean>()
    val hasLikeAnswer: LiveData<Boolean>
        get() = _hasLikeAnswer

    fun hasLike(vacancyId: Int) {
        viewModelScope.launch {
            _hasLikeAnswer.postValue(selectedVacanciesInteractor.hasLike(vacancyId))
        }
    }

    private val _favouriteVacancy = MutableLiveData<Vacancy>()
    val favouriteVacancy: LiveData<Vacancy>
        get() = _favouriteVacancy

    fun getVacancy(vacancyId: Int) {
        viewModelScope.launch {
            _favouriteVacancy.postValue(selectedVacanciesInteractor.getVacancy(vacancyId))
        }
    }
}
