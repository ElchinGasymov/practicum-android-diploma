package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectIndustryBinding
import ru.practicum.android.diploma.presentation.viewmodels.FilterIndustryViewModel
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.adapter.industry.IndustryAdapter

class FilterIndustryFragment : Fragment() {

    companion object {
        const val INDUSTRY_KEY = "industryKey"
        const val INDUSTRY_ITEM_KEY = "INDUSTRY_ITEM_KEY"
    }

    private val binding: FragmentSelectIndustryBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: FilterIndustryViewModel by viewModel()
    private val adapter by lazy {
        IndustryAdapter(
            onClick = { industryList ->
                viewModel.itemChecked(industryList)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupSearchFunctionality()
        setupToolbar()
        setupApplyButton()
    }

    private fun setupRecyclerView() {
        viewModel.updateListIndustries()
        binding.industryRecycleView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.industries.observe(viewLifecycleOwner) { list ->
            binding.placeholderGroup.isVisible = false
            adapter.industry = list
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            when (error) {
                ResponseData.ResponseError.NO_INTERNET,
                ResponseData.ResponseError.CLIENT_ERROR,
                ResponseData.ResponseError.SERVER_ERROR,
                ResponseData.ResponseError.NOT_FOUND -> {
                    binding.placeholderGroup.isVisible = true
                }

                else -> {}
            }
        }

        viewModel.hasSelected.observe(viewLifecycleOwner) {
            binding.applyButton.isVisible = it
        }
    }

    private fun setupSearchFunctionality() {
        binding.industrySearchQuery.doOnTextChanged { text, _, _, _ ->
            if (binding.industrySearchQuery.text.isNotEmpty()) {
                binding.searchIconLoupe.isVisible = false
                binding.clearCrossIc.isVisible = true
            } else {
                binding.searchIconLoupe.isVisible = true
                binding.clearCrossIc.isVisible = false
            }
            viewModel.search(text.toString())
        }

        binding.clearCrossIc.setOnClickListener {
            binding.industrySearchQuery.text.clear()
        }
    }

    private fun setupToolbar() {
        binding.industryFilterToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupApplyButton() {
        binding.applyButton.setOnClickListener {
            val json = Gson().toJson(viewModel.selectedIndustry.value)
            setFragmentResult(INDUSTRY_KEY, bundleOf(INDUSTRY_ITEM_KEY to json))
            findNavController().popBackStack()
        }
    }
}
