package ru.practicum.android.diploma.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.SearchItemViewBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    private val onClick: (Vacancy) -> Unit,
    private val onLongClick: (Vacancy) -> Unit = {}
) : RecyclerView.Adapter<VacancyViewHolder>() {
    val vacancies = ArrayList<Vacancy>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VacancyViewHolder(SearchItemViewBinding.inflate(layoutInflater, parent, false), onClick, onLongClick)
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    fun setVacancies(newList: List<Vacancy>?) {
        val compare = DiffCallback(vacancies, newList ?: emptyList())
        val diffResult = DiffUtil.calculateDiff(compare)
        vacancies.clear()
        if (!newList.isNullOrEmpty()) {
            vacancies.addAll(newList)
        }
        diffResult.dispatchUpdatesTo(this)
    }
}
