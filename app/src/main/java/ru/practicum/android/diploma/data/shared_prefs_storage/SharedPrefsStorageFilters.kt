package ru.practicum.android.diploma.data.shared_prefs_storage

import ru.practicum.android.diploma.data.dto.SaveFiltersSharedPrefsDto

interface SharedPrefsStorageFilters {
    suspend fun read(): SaveFiltersSharedPrefsDto?

    suspend fun write(filters: SaveFiltersSharedPrefsDto)

    suspend fun clear()
}
