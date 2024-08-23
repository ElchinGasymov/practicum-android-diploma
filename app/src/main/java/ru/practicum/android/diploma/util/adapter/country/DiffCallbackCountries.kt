package ru.practicum.android.diploma.util.adapter.country

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.domain.models.Country

class DiffCallbackCountries(
    private val oldList: List<Country>,
    private val newList: List<Country>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldCountry = oldList[oldItemPosition]
        val newCountry = newList[newItemPosition]
        return oldCountry == newCountry
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldCountry = oldList[oldItemPosition]
        val newCountry = newList[newItemPosition]
        return oldCountry == newCountry
    }
}
