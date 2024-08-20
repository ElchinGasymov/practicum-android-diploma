package ru.practicum.android.diploma.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.SharedPrefsRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

class SharedPrefsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SharedPrefsRepository {
    override suspend fun readSharedPrefs(): SaveFiltersSharedPrefs {
        val json = sharedPreferences.getString(HISTORY, null) ?: return SaveFiltersSharedPrefs(
            Industries("","",false),
            Country("",""),
            Region("","",null),
            "",
            false
        )
        return gson.fromJson(json, SaveFiltersSharedPrefs::class.java)
    }

    override suspend fun writeSharedPrefs(filters: SaveFiltersSharedPrefs) {
        val oldShared = readSharedPrefs()
        val newShared = oldShared.copy(
            industries = filters.industries ?: oldShared.industries,
            country = filters.country ?: oldShared.country,
            region = filters.region ?: oldShared.region,
            currency = filters.currency ?: oldShared.currency,
            noCurrency = filters.noCurrency
        )
        sharedPreferences.edit().putString(HISTORY, gson.toJson(newShared)).apply()
    }

    override suspend fun clearSharedPrefs() {
        sharedPreferences.edit().remove(HISTORY).apply()
    }

    companion object {
        private const val HISTORY = "history"
    }
}
