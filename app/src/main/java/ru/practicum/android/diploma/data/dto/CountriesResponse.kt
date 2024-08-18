package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.CountryDto

data class CountriesResponse(
    val countries: List<CountryDto>
) : Response()
