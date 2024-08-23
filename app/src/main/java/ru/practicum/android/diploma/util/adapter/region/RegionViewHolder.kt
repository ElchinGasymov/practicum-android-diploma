package ru.practicum.android.diploma.util.adapter.region

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaItemViewBinding
import ru.practicum.android.diploma.domain.models.Region

class RegionViewHolder(
    private val binding: AreaItemViewBinding,
    private val onClick: (Region) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Region) {
        binding.country.text = item.name
        binding.root.setOnClickListener {
            onClick(item)
        }
    }
}
