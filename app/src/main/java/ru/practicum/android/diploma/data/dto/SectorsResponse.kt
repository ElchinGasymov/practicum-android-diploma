package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.components.SectorDto

data class SectorsResponse(
    val sectors: List<SectorDto>
) : Response()
