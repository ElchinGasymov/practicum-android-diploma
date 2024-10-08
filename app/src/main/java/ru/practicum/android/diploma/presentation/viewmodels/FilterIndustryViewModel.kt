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
            val newList = list.value.map {
                if (industry.id == it.id) {
                    _selectedIndustry.postValue(it)
                    _hasSelected.postValue(true)
                    selectedId = it.id
                    Industries(it.id, it.name, true)
                } else {
                    it
                }
            }
            _industries.postValue(newList)
        } else {
            _industries.postValue(list.value)
        }
    }

    fun search(request: String) {
        val sortedList = listOfIndustries.mapNotNull {
            if (it.name.contains(request, true)) {
                it
            } else {
                null
            }
        }
        val newSortedList = sortedList.map {
            if (it.id == selectedId) {
                _selectedIndustry.postValue(it)
                _hasSelected.postValue(true)
                Industries(it.id, it.name, true)
            } else {
                it
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

