package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.ui.state.CountriesScreenState
import ru.practicum.android.diploma.util.ResponseData

class FilterCountryViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private val countriesStateLiveData = MutableLiveData<CountriesScreenState>()

    fun render(): LiveData<CountriesScreenState> {
        return countriesStateLiveData
    }

    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            setState(CountriesScreenState.Loading)
            filterInteractor
                .getCountries()
                .collect { response ->
                    when (response) {
                        is ResponseData.Data -> {
                            val listOfCountries = response.value.sortedBy { it.id }
                            setState(CountriesScreenState.Success(listOfCountries))
                        }
                        is ResponseData.Error -> {
                            setState(CountriesScreenState.Error(response.error))
                        }
                    }
                }
        }
    }

    private fun setState(state: CountriesScreenState) {
        countriesStateLiveData.postValue(state)
    }
}
