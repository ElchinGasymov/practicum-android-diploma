package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.ui.state.RegionsScreenState
import ru.practicum.android.diploma.util.ResponseData

class FilterRegionViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val regionsStateLiveData = MutableLiveData<RegionsScreenState>()

    fun render(): LiveData<RegionsScreenState> {
        return regionsStateLiveData
    }

    private fun setState(state: RegionsScreenState) {
        regionsStateLiveData.postValue(state)
    }

    fun getRegions(regionId: String) {
        setState(RegionsScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            if (regionId.isNotEmpty()) {
                filterInteractor
                    .getRegions(regionId)
                    .collect { response ->
                        when (response) {
                            is ResponseData.Data -> {
                                val listOfRegions = response.value.sortedBy { it.name }
                                setState(RegionsScreenState.Success(listOfRegions))
                            }

                            is ResponseData.Error -> {
                                setState(RegionsScreenState.Error(response.error))
                            }
                        }
                    }
            } else {
                filterInteractor
                    .getAllRegions()
                    .collect { response ->
                        when (response) {
                            is ResponseData.Data -> {
                                val listOfRegions = response.value.sortedBy { it.name }
                                setState(RegionsScreenState.Success(listOfRegions))
                            }

                            is ResponseData.Error -> {
                                setState(RegionsScreenState.Error(response.error))
                            }
                        }
                    }
            }
        }
    }
}
