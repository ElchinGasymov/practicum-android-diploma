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

    private val _favouriteVacancy = MutableLiveData<Boolean>()
    val favouriteVacancy: LiveData<Boolean>
        get() = _favouriteVacancy


    fun favouriteOrNot(vacancyId: Long) {
        viewModelScope.launch {
            val vacancy = selectedVacanciesInteractor.getVacancy(vacancyId)
            if (vacancy == null) {
                _favouriteVacancy.postValue(false)
            } else {
                _favouriteVacancy.postValue(true)
            }
        }
    }

}
