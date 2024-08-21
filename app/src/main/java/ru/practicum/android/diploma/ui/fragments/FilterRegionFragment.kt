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
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.viewmodels.FilterRegionViewModel
import ru.practicum.android.diploma.ui.state.RegionsScreenState
import ru.practicum.android.diploma.util.App.Companion.REGION_ID_KEY
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.adapter.region.RegionAdapter

class FilterRegionFragment : Fragment() {
    companion object {
        const val REGION_REQUEST_KEY = "REGION_REQUEST_KEY"
        const val REGION_BUNDLE_KEY = "REGION_BUNDLE_KEY"
    }

    private val binding: FragmentSelectRegionBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FilterRegionViewModel>()
    private val adapter = RegionAdapter {
        onItemClicked(it)
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
        setFragmentResultListener(REGION_ID_KEY) { _, bundle ->
            val region = bundle.getString(REGION_BUNDLE_KEY).toString()
            viewModel.getRegions(region)
        }
        binding.selectRegionToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.clearCrossIc.setOnClickListener {
            binding.regionSearchQuery.text.clear()
        }
        binding.regionSearchQuery.doOnTextChanged { text, _, _, _ ->
            if (text?.isNotEmpty() == true) {
                binding.searchIconLoupe.isVisible = false
                binding.clearCrossIc.isVisible = true
            } else {
                binding.searchIconLoupe.isVisible = true
                binding.clearCrossIc.isVisible = false
            }
            search(text.toString())
        }
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                RegionsScreenState.Default -> {}
                is RegionsScreenState.Error -> {
                    handleErrorState(state)
                }

                RegionsScreenState.Loading -> {
                    removePlaceholders()
                    startProgressBar()
                }

                is RegionsScreenState.Success -> {
                    handleSuccessState(state)
                }
            }

        }
        setAdapter()
    }

    private fun handleErrorState(state: RegionsScreenState.Error) {
        stopProgressBar()
        when (state.error) {
            ResponseData.ResponseError.NO_INTERNET,
            ResponseData.ResponseError.CLIENT_ERROR,
            ResponseData.ResponseError.SERVER_ERROR,
            ResponseData.ResponseError.NOT_FOUND -> {
                setNoRegionsState()
            }
        }
    }

    private fun handleSuccessState(state: RegionsScreenState.Success) {
        stopProgressBar()
        removePlaceholders()
        adapter.setRegions(state.regions)
        binding.regionRecycleView.isVisible = true
    }
    private fun setAdapter() {
        val regionsRecyclerView = binding.regionRecycleView
        regionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        regionsRecyclerView.adapter = adapter
    }

    private fun onItemClicked(region: Region) {
        val json = Gson().toJson(region)
        setFragmentResult(
            REGION_REQUEST_KEY,
            bundleOf(REGION_BUNDLE_KEY to json)
        )
        findNavController().navigateUp()
    }

    private fun search(query: String) {
        viewModel.search(query)
    }

    private fun startProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun stopProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun removePlaceholders() {
        binding.serverError.isVisible = false
        binding.serverErrorText.isVisible = false
        binding.noConnectionPlaceholder.isVisible = false
        binding.noConnectionText.isVisible = false
        binding.noListPlaceholderGroup.isVisible = false
        binding.regionRecycleView.isVisible = false
    }

    private fun setNoRegionsState() {
        binding.regionRecycleView.isVisible = false
        binding.noListPlaceholderGroup.isVisible = true
    }
}
