package ru.practicum.android.diploma.util.adapter.industry

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemViewBinding
import ru.practicum.android.diploma.domain.models.Industries

class IndustryViewHolder(
    private val binding: IndustryItemViewBinding,
    private val onClick: (Industries) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Industries) {
        binding.industryUnit.text = item.name
        binding.roundButton.isChecked = item.isChecked
        binding.root.setOnClickListener {
            onClick(item)
        }
    }
}
