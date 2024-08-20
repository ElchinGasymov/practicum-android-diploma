package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs
import ru.practicum.android.diploma.ui.state.FilterScreenState

class FilterViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<FilterScreenState>()

    private val _noCurrency = MutableLiveData<Boolean?>()
    private val _industry = MutableLiveData<Industries?>()
    private var region = Region("", "", null)
    private var country = Country("", "")
    private var industries = Industries("", "", false)

    fun render(): LiveData<FilterScreenState> {
        return screenStateLiveData
    }

    fun saveFilterAndClose(filter: SaveFiltersSharedPrefs) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.writeSharedPrefs(filter)
            setState(FilterScreenState.FiltersSaved(filter))
        }
    }

    fun getRegion(): Region {
        return region
    }

    fun getCountry(): Country {
        return country
    }

    fun getIndustry(): Industries {
        return industries
    }

    fun setRegion(region: Region) {
        this.region = region
    }

    fun setCountry(country: Country) {
        this.country = country
    }

    fun setIndustry(industries: Industries) {
        this.industries = industries
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
                if (filters.region != null && filters.region.id.isNotEmpty()) {
                    region = filters.region
                }
                if (filters.country != null && filters.country.id.isNotEmpty()) {
                    country = filters.country
                }
                if (filters.industries != null && filters.industries.id.isNotEmpty()) {
                    industries = filters.industries
                }
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

    fun setNoCurrencySelected(answer: Boolean) {
        _noCurrency.postValue(answer)
    }

    fun setIndustrySelected(industries: Industries?) {
        _industry.postValue(industries)
    }
}
