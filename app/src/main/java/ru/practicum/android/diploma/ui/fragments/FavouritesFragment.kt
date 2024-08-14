package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouritesBinding
import ru.practicum.android.diploma.presentation.viewmodels.favourites.FavouritesDbState
import ru.practicum.android.diploma.presentation.viewmodels.favourites.FavouritesFragmentViewModel
import ru.practicum.android.diploma.ui.fragments.SearchFragment.Companion.VACANCY_KEY
import ru.practicum.android.diploma.util.adapter.VacancyAdapter

class FavouritesFragment : Fragment() {

    private val viewModel: FavouritesFragmentViewModel by viewModel()
    private val adapter by lazy {
        VacancyAdapter(
            onClick = { vacancy ->
                findNavController().navigate(
                    R.id.action_favoriteFragment_to_vacancyFragment,
                    bundleOf(VACANCY_KEY to vacancy.id)
                )
            }
        )
    }
    private val binding: FragmentFavouritesBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rwVacancy.adapter = adapter
        viewModel.listVacancy.observe(viewLifecycleOwner) { state ->
            when (state.state) {
                FavouritesDbState.ERROR -> {
                    binding.widgetNothing.isVisible = false
                    binding.widgetWrong.isVisible = true
                    binding.rwVacancy.isVisible = false
                }

                FavouritesDbState.EMPTY -> {
                    binding.widgetNothing.isVisible = true
                    binding.widgetWrong.isVisible = false
                    binding.rwVacancy.isVisible = false
                }

                FavouritesDbState.SUCCESSFUL -> {
                    binding.widgetNothing.isVisible = false
                    binding.widgetWrong.isVisible = false
                    binding.rwVacancy.isVisible = true
                    adapter.setVacancies(state.list)
                }
            }
        }

        viewModel.getVacancyList()
    }
}
