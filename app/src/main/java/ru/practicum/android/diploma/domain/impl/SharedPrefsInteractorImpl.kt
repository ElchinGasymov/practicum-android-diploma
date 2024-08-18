package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.SharedPrefsInteractor
import ru.practicum.android.diploma.domain.SharedPrefsRepository
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

class SharedPrefsInteractorImpl(
    private val repository: SharedPrefsRepository
) : SharedPrefsInteractor {
    override suspend fun read(): SaveFiltersSharedPrefs? {
        return repository.read()
    }

    override suspend fun write(filters: SaveFiltersSharedPrefs) {
        repository.write(filters)
    }

    override suspend fun clear() {
        repository.clear()
    }
}
