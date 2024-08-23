package ru.practicum.android.diploma.util.adapter.region

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.domain.models.Region

class DiffCallbackRegions(
    private val oldList: List<Region>,
    private val newList: List<Region>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldRegion = oldList[oldItemPosition]
        val newRegion = newList[newItemPosition]
        return oldRegion == newRegion
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldRegion = oldList[oldItemPosition]
        val newRegion = newList[newItemPosition]
        return oldRegion == newRegion
    }
}
