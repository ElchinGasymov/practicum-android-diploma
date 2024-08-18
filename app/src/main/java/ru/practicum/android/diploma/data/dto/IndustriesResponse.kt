package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.IndustriesDto

data class IndustriesResponse(
    val sectors: List<IndustriesDto>
) : Response()
