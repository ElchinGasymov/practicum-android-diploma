package ru.practicum.android.diploma.presentation.viewmodels.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FavouriteVacanciesInteractor

class FavouritesFragmentViewModel(
    private val favouriteVacanciesInteractor: FavouriteVacanciesInteractor
) : ViewModel() {

    private val _listVacancy = MutableLiveData<FavouritesScreenState>()
    val listVacancy: LiveData<FavouritesScreenState>
        get() = _listVacancy

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
}
