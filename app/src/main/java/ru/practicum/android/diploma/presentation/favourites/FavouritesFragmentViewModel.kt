package ru.practicum.android.diploma.presentation.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouritesFragmentViewModel(
    private val favouriteVacanciesInteractor: FavouriteVacanciesInteractor
) : ViewModel() {

    private val _listVacancy = MutableLiveData<FavouritesScreenState>()
    val listVacancy: LiveData<FavouritesScreenState>
        get() = _listVacancy

    private val _favouriteVacancy = MutableLiveData<VacancyDetails>()
    val favouriteVacancy: LiveData<VacancyDetails>
        get() = _favouriteVacancy

    private val _hasLikeAnswer = MutableLiveData<Boolean>()
    val hasLikeAnswer: LiveData<Boolean>
        get() = _hasLikeAnswer

    fun getVacancyList() {
        viewModelScope.launch {
            favouriteVacanciesInteractor.listVacancies()
                .catch { exception ->

                    _listVacancy.postValue(FavouritesScreenState(FavouritesDbState.ERROR, emptyList()))
                }.collect { list ->
                    if (list.isEmpty()) {
                        _listVacancy.postValue(FavouritesScreenState(FavouritesDbState.EMPTY, emptyList()))
                    } else {
                        _listVacancy.postValue(FavouritesScreenState(FavouritesDbState.SUCCESSFUL, list))
                    }
                }
        }
    }

    fun addVacancy(vacancy: VacancyDetails) {
        viewModelScope.launch {
            favouriteVacanciesInteractor.addVacancy(vacancy)
        }
    }

    fun deleteVacancy(vacancyId: String) {
        viewModelScope.launch {
            favouriteVacanciesInteractor.deleteVacancy(vacancyId)
        }
    }

    fun hasLike(vacancyId: String) {
        viewModelScope.launch {
            _hasLikeAnswer.postValue(favouriteVacanciesInteractor.hasLike(vacancyId))
        }
    }

    fun getVacancy(vacancyId: String) {
        viewModelScope.launch {
            _favouriteVacancy.postValue(favouriteVacanciesInteractor.getVacancy(vacancyId))
        }
    }
}
