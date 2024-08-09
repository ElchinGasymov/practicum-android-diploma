package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.FavouriteVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.db.FavouriteVacanciesRepository

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
}
