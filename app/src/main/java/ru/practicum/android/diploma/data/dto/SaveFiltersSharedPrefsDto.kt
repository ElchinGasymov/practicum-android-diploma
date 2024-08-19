package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.CountryDto
import ru.practicum.android.diploma.data.dto.components.IndustriesDto
import ru.practicum.android.diploma.data.dto.components.RegionDto
import java.io.Serializable

data class SaveFiltersSharedPrefsDto(
    val industries: IndustriesDto?,
    val country: CountryDto?,
    val region: RegionDto?,
    val currency: Int?,
    val noCurrency: Boolean?
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
