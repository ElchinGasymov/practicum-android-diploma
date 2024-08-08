package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.SelectedVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.db.SelectedVacanciesRepository

val repositoryModule = module {
    single<SelectedVacanciesRepository> {
        SelectedVacanciesRepositoryImpl(
            vacanciesDatabase = get(),
            converterModel = get(),
            converterIntoEntity = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }
}
