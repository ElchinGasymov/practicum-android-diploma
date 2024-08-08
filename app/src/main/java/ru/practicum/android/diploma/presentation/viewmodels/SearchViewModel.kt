package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.ui.state.SearchScreenState
import ru.practicum.android.diploma.util.ITEMS_PER_PAGE
import ru.practicum.android.diploma.util.Options
import ru.practicum.android.diploma.util.ResponseData

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
                            if (response.value.results.isEmpty()) {
                                setScreenState(SearchScreenState.NothingFound)
                            } else {
                                maxPages = response.value.pages
                                currentPage = response.value.page
                                if (isNewRequest) {
                                    setScreenState(
                                        SearchScreenState.Success(response.value.results, response.value.foundVacancies)
                                    )
                                } else {
                                    setScreenState(SearchScreenState.LoadNextPage(response.value.results))
                                }
                            }
                        }
                        is ResponseData.Error -> {
                            if (isNewRequest) {
                                setScreenState(SearchScreenState.Error(response.error))
                            } else {
                                setScreenState(SearchScreenState.ErrorNextPage(response.error))
                            }

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
}
