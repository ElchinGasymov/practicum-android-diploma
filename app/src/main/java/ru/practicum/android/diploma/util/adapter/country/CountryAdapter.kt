package ru.practicum.android.diploma.util.adapter.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaItemViewBinding
import ru.practicum.android.diploma.domain.models.Country

class CountryAdapter(
    private val onClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryViewHolder>() {
    private val countries = ArrayList<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CountryViewHolder(AreaItemViewBinding.inflate(layoutInflater, parent, false), onClick)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    fun setCountries(newList: List<Country>?) {
        val compare = DiffCallbackCountries(countries, newList ?: emptyList())
        val diffResult = DiffUtil.calculateDiff(compare)
        countries.clear()
        if (!newList.isNullOrEmpty()) {
            countries.addAll(newList)
        }
        diffResult.dispatchUpdatesTo(this)
    }
}
