package ru.practicum.android.diploma.util.extension

import android.os.Bundle
import androidx.core.os.BundleCompat

inline fun <reified T> Bundle.parcelable(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)
