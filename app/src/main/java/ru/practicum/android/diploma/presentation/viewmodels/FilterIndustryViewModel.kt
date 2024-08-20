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

    private val _hasSelected = MutableLiveData(false)
    val hasSelected: LiveData<Boolean>
        get() = _hasSelected

    private val _selectedIndustry = MutableLiveData<Industries>()
    val selectedIndustry: LiveData<Industries>
        get() = _selectedIndustry

  /*  fun writeSharedPrefs() {
        viewModelScope.launch {
            interactor.writeSharedPrefs(
                SaveFiltersSharedPrefs(
                    industries = _selectedIndustry.value,
                    null,
                    null,
                    null,
                    false
                )
            )
        }
    }*/

    fun itemChecked(industries: Industries) {
        val newList = ArrayList<Industries>()
        _industries.value?.forEach { industry ->
            if (industry.id == industries.id) {
                newList.add(industry.copy(isChecked = !industry.isChecked))
                _hasSelected.postValue(!industry.isChecked)
                if (!industry.isChecked) _selectedIndustry.postValue(industry)
            } else {
                newList.add(industry.copy(isChecked = false))
            }
        }
        _industries.postValue(newList)
    }

    fun updateListIndustries() {
        viewModelScope.launch {
            val industry = interactor.readSharedPrefs()?.industries
            interactor.getIndustries().collect { list ->
                when (list) {
                    is ResponseData.Data -> {
                        whenList(industry, list)
                    }

                    is ResponseData.Error -> {}
                }
            }
        }
    }

    fun whenList(industry: Industries?, list: ResponseData.Data<List<Industries>>) {
        if (industry != null) {
            val newList = ArrayList<Industries>()
            list.value.forEach { industryItem ->
                if (industryItem.id == industry.id) {
                    newList.add(industryItem.copy(isChecked = true))
                    _selectedIndustry.postValue(industryItem)
                    _hasSelected.postValue(true)
                } else {
                    newList.add(industryItem)
                }
            }
            _industries.postValue(newList)
        } else {
            _industries.postValue(list.value)
        }
    }
}

