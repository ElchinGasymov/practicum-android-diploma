package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.RegionDto

data class RegionsResponse(
    val regions: List<RegionDto>
) : Response()
