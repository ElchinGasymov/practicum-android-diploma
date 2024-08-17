package ru.practicum.android.diploma.data.dto.components

import com.google.gson.annotations.SerializedName

data class RegionDto(
    val id: String,
    val name: String,
    @SerializedName("parent_id") val parentId: Int?
)
