package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.AllRegionInteractor
import ru.practicum.android.diploma.domain.CountryInteractor
import ru.practicum.android.diploma.domain.DetailsInteractor
import ru.practicum.android.diploma.domain.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.RegionInteractor
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.impl.AllRegionInteractorImpl
import ru.practicum.android.diploma.domain.impl.CountryInteractorImpl
import ru.practicum.android.diploma.domain.impl.DetailsInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavouriteVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.domain.impl.RegionInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl

val interactorModule = module {
    factory<FavouriteVacanciesInteractor> {
        FavouriteVacanciesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    factory<DetailsInteractor> {
        DetailsInteractorImpl(repository = get())
    }

    factory<CountryInteractor> {
        CountryInteractorImpl(repository = get())
    }

    factory<RegionInteractor> {
        RegionInteractorImpl(repository = get())
    }

    factory<AllRegionInteractor> {
        AllRegionInteractorImpl(repository = get())
    }

    factory<IndustryInteractor> {
        IndustryInteractorImpl(repository = get())
    }
}
