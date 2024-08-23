package ru.practicum.android.diploma.util.adapter.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemViewBinding
import ru.practicum.android.diploma.domain.models.Industries

class IndustryAdapter(
    private val onClick: (Industries) -> Unit
) : RecyclerView.Adapter<IndustryViewHolder>() {
    val industries = mutableListOf<Industries>()
    var industry: List<Industries> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IndustryViewHolder(IndustryItemViewBinding.inflate(layoutInflater, parent, false), onClick)
    }

    override fun getItemCount(): Int {
        return industry.size
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industry[position])
    }

}
