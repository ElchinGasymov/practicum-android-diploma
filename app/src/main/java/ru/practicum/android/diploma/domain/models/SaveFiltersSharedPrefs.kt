package ru.practicum.android.diploma.domain.models


data class SaveFiltersSharedPrefs(
    val industries: Industries?,
    val country: Country?,
    val region: Region?,
    val currency: Int?,
    val noCurrency: Boolean?
)
