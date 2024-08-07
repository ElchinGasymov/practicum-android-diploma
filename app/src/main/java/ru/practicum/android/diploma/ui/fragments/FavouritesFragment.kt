package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouritesBinding
import ru.practicum.android.diploma.presentation.FavouritesFragmentViewModel

class FavouritesFragment : Fragment() {

    private val viewModel: FavouritesFragmentViewModel by viewModel()
    private val adapter by lazy { VacancyAdapter() }
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
        viewModel.listVacancy.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.widgetNothing.isVisible = true
                binding.widjetWrong.isVisible = false
                binding.rwVacancy.isVisible = false
            } else {
                binding.widgetNothing.isVisible = false
                binding.widjetWrong.isVisible = false
                binding.rwVacancy.isVisible = true
                adapter.data = it
            }
        }
        viewModel.getVacancyList()
        adapter.onClick = { item ->
            onClickAdapter(item)
        }
    }

    private fun onClickAdapter(vacancy: Vacancy) {
        findNavController().navigate(
            R.id.action_favoriteFragment_to_vacancyFragment
        )
    }
}
