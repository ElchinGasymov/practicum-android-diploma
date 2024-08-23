package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.ResponseData.ResponseError

class FilterIndustryViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {
    private val _industries = MutableLiveData<List<Industries>>()
    private val listOfIndustries = mutableListOf<Industries>()
    private var selectedId = ""
    val industries: LiveData<List<Industries>>
        get() = _industries

    private val _hasSelected = MutableLiveData(false)
    val hasSelected: LiveData<Boolean>
        get() = _hasSelected

    private val _selectedIndustry = MutableLiveData<Industries>()
    val selectedIndustry: LiveData<Industries>
        get() = _selectedIndustry
    private val _error = MutableLiveData<ResponseError>()
    val error: LiveData<ResponseError>
        get() = _error

    fun itemChecked(industries: Industries) {
        val newList = ArrayList<Industries>()
        _industries.value?.forEach { industry ->
            if (industry.id == industries.id) {
                newList.add(industry.copy(isChecked = !industry.isChecked))
                _hasSelected.postValue(!industry.isChecked)
                selectedId = industries.id
                if (!industry.isChecked) _selectedIndustry.postValue(industry)
            } else {
                newList.add(industry.copy(isChecked = false))
            }
        }
        _industries.postValue(newList)
    }

    fun updateListIndustries() {
        viewModelScope.launch(Dispatchers.IO) {
            val industry = interactor.readSharedPrefs()?.industries
            interactor.getIndustries().collect { list ->
                when (list) {
                    is ResponseData.Data -> {
                        whenList(industry, list)
                        listOfIndustries.clear()
                        listOfIndustries.addAll(list.value)
                    }

                    is ResponseData.Error -> {
                        _error.postValue(list.error)
                    }
                }
            }
        }
    }

    private fun whenList(industry: Industries?, list: ResponseData.Data<List<Industries>>) {
        if (industry != null) {
            val newList = ArrayList<Industries>()
            list.value.forEach { industryItem ->
                if (industryItem.id == industry.id) {
                    newList.add(industryItem.copy(isChecked = true))
                    _selectedIndustry.postValue(industryItem)
                    _hasSelected.postValue(true)
                    selectedId = industryItem.id
                } else {
                    newList.add(industryItem)
                }
            }
            _industries.postValue(newList)
        } else {
            _industries.postValue(list.value)
        }
    }

    fun search(request: String) {
        val sortedList = mutableListOf<Industries>()
        val newSortedList = mutableListOf<Industries>()
        sortedList.addAll(listOfIndustries)
        sortedList.removeAll {
            !it.name.contains(request, true)
        }
        sortedList.forEach {
            if (it.id == selectedId) {
                newSortedList.add(Industries(it.id, it.name, true))
                _selectedIndustry.postValue(it)
                _hasSelected.postValue(true)
            } else {
                newSortedList.add(it)
            }
        }
        if (sortedList.isNotEmpty()) {
            _industries.postValue(newSortedList)
        } else {
            _industries.postValue(newSortedList)
            _error.postValue(ResponseError.NOT_FOUND)
        }

    }
}

