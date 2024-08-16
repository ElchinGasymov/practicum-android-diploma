package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FavouriteVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.FilterRepositoryImpl
import ru.practicum.android.diploma.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.FavouriteVacanciesRepository
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {
    single<FavouriteVacanciesRepository> {
        FavouriteVacanciesRepositoryImpl(
            vacanciesDatabase = get(),
            converterIntoModel = get(),
            converterIntoEntity = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }
    single<VacancyRepository> {
        VacancyRepositoryImpl(get(), get())
    }
    single<FilterRepository> {
        FilterRepositoryImpl(networkClient = get())
    }
}
