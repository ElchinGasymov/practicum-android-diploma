package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Industries(
    val id: String,
    val name: String,
    val isChecked: Boolean
) : Parcelable
