package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectIndustryBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.presentation.viewmodels.FilterIndustryViewModel

class FilterIndustryFragment : Fragment() {

    private val binding: FragmentSelectIndustryBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: FilterIndustryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.industries.observe(viewLifecycleOwner){ list ->
            list.forEach {  }
        }

        binding.industrySearchQuery.doOnTextChanged { text, start, before, count ->
            if (binding.industrySearchQuery.text.isNotEmpty()){
                binding.searchIconLoupe.isVisible = false
                binding.clearCrossIc.isVisible = true
            } else {
                binding.searchIconLoupe.isVisible = true
                binding.clearCrossIc.isVisible = false
            }
        }

        binding.industryFilterToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}
