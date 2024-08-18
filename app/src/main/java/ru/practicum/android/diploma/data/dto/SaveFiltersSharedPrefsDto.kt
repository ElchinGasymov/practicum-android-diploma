package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.CountryDto
import ru.practicum.android.diploma.data.dto.components.IndustriesDto
import ru.practicum.android.diploma.data.dto.components.RegionDto

data class SaveFiltersSharedPrefsDto(
    val industries: IndustriesDto?,
    val country: CountryDto?,
    val region: RegionDto?,
    val currency: Int?,
    val noCurrency: Boolean?
)
