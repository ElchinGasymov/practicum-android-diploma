package ru.practicum.android.diploma.data.dto.components

data class CountryDto(
    val id: String,
    val name: String,
    val areas: List<RegionDto>
)
