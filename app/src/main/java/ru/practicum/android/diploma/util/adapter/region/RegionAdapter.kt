package ru.practicum.android.diploma.util.adapter.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaItemViewBinding
import ru.practicum.android.diploma.domain.models.Region

class RegionAdapter(
    private val onClick: (Region) -> Unit
) : RecyclerView.Adapter<RegionViewHolder>() {
    private val regions = ArrayList<Region>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RegionViewHolder(AreaItemViewBinding.inflate(layoutInflater, parent, false), onClick)
    }

    override fun getItemCount(): Int {
        return regions.size
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(regions[position])
    }

    fun setRegions(newList: List<Region>?) {
        val compare = DiffCallbackRegions(regions, newList ?: emptyList())
        val diffResult = DiffUtil.calculateDiff(compare)
        regions.clear()
        if (!newList.isNullOrEmpty()) {
            regions.addAll(newList)
        }
        diffResult.dispatchUpdatesTo(this)
    }
}
