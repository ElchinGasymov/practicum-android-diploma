package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.ui.state.PlaceOfWorkScreenState
import ru.practicum.android.diploma.util.ResponseData

class FilterPlaceOfWorkViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<PlaceOfWorkScreenState>()

    fun render(): LiveData<PlaceOfWorkScreenState> {
        return screenStateLiveData
    }

    fun getCountryName(countryId: String, isSaving: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor
                .getCountries()
                .collect { response ->
                    when (response) {
                        is ResponseData.Data -> {
                            response.value.forEach {
                                if (it.id == countryId) {
                                    setData(isSaving, it)
                                }
                            }
                        }

                        is ResponseData.Error -> {}
                    }
                }
        }
    }

    fun saveFields(countryName: String, regionId: String) {
        if (countryName.isEmpty()) {
            getCountryName(regionId, true)
        } else {
            setState(PlaceOfWorkScreenState.Saved(countryName, regionId))
        }
    }

    private fun setData(isSaving: Boolean, country: Country) {
        if (isSaving) {
            setState(PlaceOfWorkScreenState.Saved(country.name, country.id))
        } else {
            setState(PlaceOfWorkScreenState.CountryName(country.name, country.id))
        }
    }

    fun setCountryName(countryName: String, countryId: String) {
        if (countryName.isNotEmpty()) {
            setState(PlaceOfWorkScreenState.CountryName(countryName, countryId))
        } else {
            setState(PlaceOfWorkScreenState.NoCountryName)
        }
    }

    fun setRegionName(regionName: String) {
        if (regionName.isNotEmpty()) {
            setState(PlaceOfWorkScreenState.RegionName(regionName))
        } else {
            setState(PlaceOfWorkScreenState.NoRegionName)
        }
    }

    private fun setState(state: PlaceOfWorkScreenState) {
        screenStateLiveData.postValue(state)
    }
}
