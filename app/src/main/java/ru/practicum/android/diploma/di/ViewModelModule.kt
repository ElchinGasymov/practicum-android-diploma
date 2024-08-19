package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodels.FilterCountryViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FilterIndustryViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FilterPlaceOfWorkViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FilterRegionViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FilterViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.presentation.viewmodels.favourites.FavouritesFragmentViewModel

val viewModelModule = module {
    viewModel<FavouritesFragmentViewModel> {
        FavouritesFragmentViewModel(get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }

    viewModel<VacancyViewModel> {
        VacancyViewModel(get(), get())
    }

    viewModel<FilterViewModel> {
        FilterViewModel()
    }
    viewModel<FilterPlaceOfWorkViewModel> {
        FilterPlaceOfWorkViewModel(get())
    }
    viewModel<FilterCountryViewModel> {
        FilterCountryViewModel(get())
    }
    viewModel<FilterRegionViewModel> {
        FilterRegionViewModel(get())
    }
    viewModel <FilterIndustryViewModel>{
        FilterIndustryViewModel(get())
    }
}
