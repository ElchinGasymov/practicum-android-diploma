package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.DetailsInteractor
import ru.practicum.android.diploma.domain.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.state.FavouriteState
import ru.practicum.android.diploma.ui.state.VacancyScreenState
import ru.practicum.android.diploma.util.ResponseData

class VacancyViewModel(
    private val detailsInteractor: DetailsInteractor,
    private val favouritesInteractor: FavouriteVacanciesInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<VacancyScreenState>(VacancyScreenState.Default)
    private val favouriteLiveData = MutableLiveData<FavouriteState>()

    fun render(): LiveData<VacancyScreenState> {
        return screenStateLiveData
    }

    private fun setScreenState(state: VacancyScreenState) {
        screenStateLiveData.postValue(state)
    }

    fun favouriteRender(): LiveData<FavouriteState> {
        return favouriteLiveData
    }

    private fun setFavouriteState(state: FavouriteState) {
        favouriteLiveData.postValue(state)
    }

    fun onLikeClicked(vacancy: VacancyDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            val isLiked = favouritesInteractor.hasLike(vacancy.id)
            if (isLiked) {
                favouritesInteractor.deleteVacancy(vacancy.id)
                setFavouriteState(FavouriteState.NotFavourite)
            } else {
                favouritesInteractor.addVacancy(vacancy)
                setFavouriteState(FavouriteState.Favourite)
            }

        }
    }

    fun getVacancyDetails(vacancyId: String) {
        setScreenState(VacancyScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            if (favouritesInteractor.hasLike(vacancyId)) {
                setFavouriteState(FavouriteState.Favourite)
            }
            detailsInteractor
                .getVacancy(vacancyId)
                .collect { response ->
                    when (response) {
                        is ResponseData.Data -> {
                            setScreenState(VacancyScreenState.Success(response.value))
                        }

                        is ResponseData.Error -> {
                            setScreenState(VacancyScreenState.Error(response.error))
                        }
                    }
                }
        }
    }
}
