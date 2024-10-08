package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.state.SearchScreenState
import ru.practicum.android.diploma.util.Options
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    companion object {
        const val ITEMS_PER_PAGE = 20

        const val ONE_SECOND = 1000L
        const val TWO_SECONDS = 2000L
    }

    private val _vacanciesLiveData = MutableLiveData<List<Vacancy>>()
    val vacanciesLiveData: LiveData<List<Vacancy>> get() = _vacanciesLiveData
    private val _vacanciesQuantityLiveData = MutableLiveData<Int>()
    val vacanciesQuantityLiveData: LiveData<Int> get() = _vacanciesQuantityLiveData

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    private var currentPage = 0
    private var maxPages = 1
    private var isNextPageLoading = false
    private var mainRequest = ""
    private var requestNextPage = ""
    private var options = Options(
        requestNextPage,
        ITEMS_PER_PAGE,
        currentPage,
        "",
        "",
        "",
        false
    )
    private val _vacancyIsClickable = MutableLiveData(true)
    val vacancyIsClickable: LiveData<Boolean> = _vacancyIsClickable

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

    fun getMainRequest(): String {
        return mainRequest
    }

    private fun updateVacancies(vacancies: List<Vacancy>) {
        _vacanciesLiveData.postValue(vacancies)
    }

    private fun searchVacancies(request: String) {
        if (request != this.mainRequest) {
            currentPage = 0
            setScreenState(SearchScreenState.Loading)
            this.mainRequest = request
            requestNextPage = request
            getOptions()
            search(true)
        }

    }

    fun setDefaultCurrentPage() {
        currentPage = 0
    }

    fun getOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val filter = filterInteractor.readSharedPrefs()
            if (filter != null) {
                options = Options(
                    requestNextPage,
                    ITEMS_PER_PAGE,
                    0,
                    if (filter.region?.id?.isNotEmpty() == true) {
                        filter.region.id
                    } else {
                        filter.country?.id.toString()
                    },
                    filter.industries?.id.toString(),
                    filter.currency.toString(),
                    filter.noCurrency
                )
                setScreenState(SearchScreenState.Default)
            } else {
                options = Options(
                    requestNextPage,
                    ITEMS_PER_PAGE,
                    currentPage,
                    "",
                    "",
                    "",
                    false
                )
                setScreenState(SearchScreenState.Default)
            }
        }
    }

    fun search(isNewRequest: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor
                .search(
                    Options(
                        requestNextPage,
                        ITEMS_PER_PAGE,
                        currentPage,
                        options.area,
                        options.industry,
                        options.salary,
                        options.withSalary
                    )
                )
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

    fun isFilter(): Boolean {
        return options.area?.isNotEmpty() == true ||
            options.industry?.isNotEmpty() == true ||
            options.salary?.isNotEmpty() == true ||
            options.withSalary == true
    }

    fun onVacancyClicked() {
        vacancyOnClickDebounce(true)
    }

    fun searchDebounce(query: String) {
        if (query.isNotEmpty()) {
            vacancySearchDebounce(query)
        }
    }

    private fun setDataState(data: List<Vacancy>, quantity: Int, isNewRequest: Boolean) {
        if (isNewRequest) {
            if (data.isEmpty()) {
                setScreenState(SearchScreenState.NothingFound)
            } else {
                setScreenState(SearchScreenState.Success(data, quantity))
                _vacanciesQuantityLiveData.postValue(quantity)
                updateVacancies(data)
            }
        } else {
            val currentVacancies = _vacanciesLiveData.value.orEmpty().toMutableList()
            currentVacancies.addAll(data)
            updateVacancies(currentVacancies)
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
