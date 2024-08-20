package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.ui.state.FilterScreenState

class FilterViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<FilterScreenState>()

    private val _filtersSave = MutableLiveData<SaveFiltersSharedPrefs>()
    val sharedPrefs: LiveData<SaveFiltersSharedPrefs>
        get() = _filtersSave

    private val _country = MutableLiveData<Country?>()
    private val _region = MutableLiveData<Region?>()
    private val _currency = MutableLiveData<Int?>()
    private val _noCurrency = MutableLiveData<Boolean?>()
    private val _industry = MutableLiveData<Industries?>()

    fun readSharedPrefs() {
        viewModelScope.launch {
            _filtersSave.postValue(filterInteractor.readSharedPrefs())
        }
    }

    fun clearSharedPrefs() {
        viewModelScope.launch {
            filterInteractor.clearSharedPrefs()
        }
    }

    fun render(): LiveData<FilterScreenState> {
        return screenStateLiveData
    }

    fun saveFilterAndClose(filter: SaveFiltersSharedPrefs) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.writeSharedPrefs(filter)
            setState(FilterScreenState.FiltersSaved(filter))
        }
    }

    fun saveFilter(filter: SaveFiltersSharedPrefs) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.writeSharedPrefs(filter)
        }
    }

    fun getFilterSetting() {
        viewModelScope.launch(Dispatchers.IO) {
            val filters = filterInteractor.readSharedPrefs()
            if (filters != null) {
                setState(FilterScreenState.FiltersLoaded(filters))
            }
        }
    }

    fun clear() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.clearSharedPrefs()
            setState(FilterScreenState.ClearState)
        }
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

    fun setCountrySelected(country: Country) {
        _country.postValue(country)
    }

    fun setRegionSelected(region: Region) {
        _region.postValue(region)
    }

    fun setCurrencySelected(currency: Int) {
        _currency.postValue(currency)
    }

    fun saveSharedPrefsCurrency(currency: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.writeSharedPrefs(
                SaveFiltersSharedPrefs(
                    industries = null,
                    country = null,
                    region = null,
                    currency = currency.toString(),
                    noCurrency = false
                )
            )
        }
    }

    fun saveSharedPrefsNoCurrency(noCurrency: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.writeSharedPrefs(
                SaveFiltersSharedPrefs(
                    industries = null,
                    country = null,
                    region = null,
                    currency = null,
                    noCurrency = noCurrency
                )
            )
        }
    }

    fun setNoCurrencySelected(answer: Boolean) {
        _noCurrency.postValue(answer)
    }

    fun setIndustrySelected(industries: Industries?) {
        _industry.postValue(industries)
    }

}
