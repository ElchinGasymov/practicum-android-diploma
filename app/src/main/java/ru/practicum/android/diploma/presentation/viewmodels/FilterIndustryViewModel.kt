package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.util.ResponseData

class FilterIndustryViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {
    private val _industries = MutableLiveData<List<Industries>>()
    val industries: LiveData<List<Industries>>
        get() = _industries

    fun updateListIndustries() {
        viewModelScope.launch {
            interactor.getIndustries().collect { list ->
                when (list) {
                    is ResponseData.Data -> {
                        _industries.postValue(list.value)
                    }
                    is ResponseData.Error -> {}
                }
            }
        }
    }

    private fun writeSharedPrefs(industries: Industries){

    }

    private fun readSharedPrefs(){
        
    }
}

