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
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {
    companion object {
        const val ITEMS_PER_PAGE = 20

        const val ONE_SECOND = 1000L
        const val TWO_SECONDS = 2000L
    }

    private val screenStateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private var currentPage = 0
    private var maxPages = 1
    private var isNextPageLoading = false
    private var mainRequest = ""
    private var requestNextPage = ""
    var amountVacancies = 0

    private val _vacancyIsClickable = MutableLiveData(true)
    val vacancyIsClickable: LiveData<Boolean> = _vacancyIsClickable

    private val _vacancies = MutableLiveData<List<Vacancy>>(emptyList())
    val vacancies: LiveData<List<Vacancy>> = _vacancies

    private val vacancySearchDebounce =
        debounce<String>(TWO_SECONDS, viewModelScope, true) { query ->
            searchVacancies(query)
        }

    private val vacancyOnClickDebounce =
        debounce<Boolean>(ONE_SECOND, viewModelScope, false) {
            _vacancyIsClickable.value = it
        }

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

    private fun searchVacancies(request: String) {
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

    fun onVacancyClicked() {
        vacancyOnClickDebounce(true)
    }

    fun searchDebounce(query: String) {
        if (query.isNotEmpty()) {
            vacancySearchDebounce(query)
        }
    }

    fun clearVacancies() {
        _vacancies.postValue(emptyList())
    }

    private fun setDataState(data: List<Vacancy>, quantity: Int, isNewRequest: Boolean) {
        if (isNewRequest) {
            if (data.isEmpty()) {
                setScreenState(SearchScreenState.NothingFound)
            } else {
                _vacancies.postValue(data)
                amountVacancies = quantity
                setScreenState(SearchScreenState.Success(data, quantity))
            }
        } else {
            val currentList = _vacancies.value.orEmpty().toMutableList()
            currentList.addAll(data)
            _vacancies.postValue(currentList)
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
