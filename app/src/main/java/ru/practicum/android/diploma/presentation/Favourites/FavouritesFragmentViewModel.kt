package ru.practicum.android.diploma.presentation.Favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouritesFragmentViewModel(
    private val selectedVacanciesInteractor: SelectedVacanciesInteractor
) : ViewModel() {

    private val _listVacancy = MutableLiveData<FavouritesScreenState>()
    val listVacancy: LiveData<FavouritesScreenState>
        get() = _listVacancy

    fun getVacancyList() {
        viewModelScope.launch {
            selectedVacanciesInteractor.listVacancies()
                .catch{ exception ->
                    _listVacancy.postValue(FavouritesScreenState(FavouritesDbState.ERROR, emptyList()))
            }.collect { list ->
                if (list.isEmpty){
                    _listVacancy.postValue(FavouritesScreenState(FavouritesDbState.EMPTY, emptyList()))
                } else {
                    _listVacancy.postValue(FavouritesScreenState(FavouritesDbState.SUCCESSFUL, list))
                }
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
