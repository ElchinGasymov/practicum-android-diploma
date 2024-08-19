package ru.practicum.android.diploma.util.adapter.country

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaItemViewBinding
import ru.practicum.android.diploma.domain.models.Country

class CountryViewHolder(
    private val binding: AreaItemViewBinding,
    private val onClick: (Country) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Country) {
        binding.country.text = item.name
        binding.root.setOnClickListener {
            onClick(item)
        }
    }
}
