package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.state.SearchScreenState
import ru.practicum.android.diploma.util.Options
import ru.practicum.android.diploma.util.ResponseData

const val ITEMS_PER_PAGE = 20

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private var currentPage = 0
    private var maxPages = 1
    private var isNextPageLoading = false
    private var mainRequest = ""
    private var requestNextPage = ""

    fun render(): LiveData<SearchScreenState> {
        return screenStateLiveData
    }

    private fun setScreenState(state: SearchScreenState) {
        screenStateLiveData.postValue(state)
    }

    fun setRequest(request: String) {
        this.mainRequest = request
    }

    fun onStart() {
        setScreenState(SearchScreenState.Default)
    }

    fun searchVacancies(request: String) {
        if (request != this.mainRequest) {
            currentPage = 0
            setScreenState(SearchScreenState.Loading)
            this.mainRequest = request
            requestNextPage = request
            search(true)
        }

    }

    private fun search(isNewRequest: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor
                .search(Options(requestNextPage, ITEMS_PER_PAGE, currentPage))
                .collect { response ->
                    when (response) {
                        is ResponseData.Data -> {
                            maxPages = response.value.pages
                            currentPage = response.value.page
                            setDataState(response.value.results, response.value.foundVacancies, isNewRequest)
                        }

                        is ResponseData.Error -> {
                            setErrorState(response.error, isNewRequest)
                        }
                    }
                }
            isNextPageLoading = false
        }
    }

    fun onLastItemReached() {
        if (!isNextPageLoading) {
            currentPage++
            if (currentPage < maxPages) {
                isNextPageLoading = true
                setScreenState(SearchScreenState.LoadingNextPage)
                search(false)
            }
        }
    }

    private fun setDataState(data: List<Vacancy>, quantity: Int, isNewRequest: Boolean) {
        if (isNewRequest) {
            if (data.isEmpty()) {
                setScreenState(SearchScreenState.NothingFound)
            } else {
                setScreenState(
                    SearchScreenState.Success(data, quantity)
                )
            }
        } else {
            setScreenState(SearchScreenState.LoadNextPage(data))
        }
    }

    private fun setErrorState(error: ResponseData.ResponseError, isNewRequest: Boolean) {
        if (isNewRequest) {
            setScreenState(SearchScreenState.Error(error))
        } else {
            setScreenState(SearchScreenState.ErrorNextPage(error))
        }
    }
}
