package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.ui.state.FilterScreenState

class FilterViewModel : ViewModel() {

    private val screenStateLiveData = MutableLiveData<FilterScreenState>()

    fun render(): LiveData<FilterScreenState> {
        return screenStateLiveData
    }

    fun saveFilter() {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

    fun clear() {
        setState(FilterScreenState.ClearState)
    }

    fun setIndustry(industry: String) {
        if (industry.isNotEmpty()) {
            setState(FilterScreenState.Industry(industry))
        } else {
            setState(FilterScreenState.NoIndustry)
        }

    }

    private fun setState(state: FilterScreenState) {
        screenStateLiveData.postValue(state)
    }

    fun setPlaceOfWork(country: String) {
        if (country.isNotEmpty()) {
            setState(FilterScreenState.PlaceOfWork(country))
        } else {
            setState(FilterScreenState.NoPlaceOfWork)
        }

    }

}
