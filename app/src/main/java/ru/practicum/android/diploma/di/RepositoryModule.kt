package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository


val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }
    single<VacancyRepository> {
        VacancyRepositoryImpl(get())
    }
}
