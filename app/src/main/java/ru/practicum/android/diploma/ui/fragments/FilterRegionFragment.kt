package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.viewmodels.FilterRegionViewModel
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.REGION_ID_KEY
import ru.practicum.android.diploma.ui.state.RegionsScreenState
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.adapter.region.RegionAdapter

class FilterRegionFragment : Fragment() {

    companion object {
        const val REGION_REQUEST_KEY = "REGION_REQUEST_KEY"
        const val REGION_NAME_KEY = "REGION_BUNDLE_KEY"
        const val REGION_PARENT_ID_KEY = "REGION_PARENT_ID_KEY"
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
            val region = bundle.getString(REGION_NAME_KEY).toString()
            viewModel.getRegions(region)
        }
        binding.selectRegionToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                RegionsScreenState.Default -> {}
                is RegionsScreenState.Error -> {
                    stopProgressBar()
                    when (state.error) {
                        ResponseData.ResponseError.NO_INTERNET -> {
                            setNoInternetState()
                        }

                        ResponseData.ResponseError.CLIENT_ERROR,
                        ResponseData.ResponseError.SERVER_ERROR -> {
                            setServerErrorState()
                        }

                        ResponseData.ResponseError.NOT_FOUND -> {
                            setNoRegionsState()
                        }
                    }
                }

                RegionsScreenState.Loading -> {
                    removePlaceholders()
                    startProgressBar()
                }

                is RegionsScreenState.Success -> {
                    stopProgressBar()
                    adapter.setRegions(state.regions)
                }
            }

        }

        val regionsRecyclerView = binding.regionRecycleView
        regionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        regionsRecyclerView.adapter = adapter
    }

    private fun onItemClicked(region: Region) {
        setFragmentResult(REGION_REQUEST_KEY, bundleOf(
                REGION_NAME_KEY to region.name,
                REGION_PARENT_ID_KEY to region.parentId
            )
        )
        findNavController().navigateUp()
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
    }

    private fun setServerErrorState() {
        binding.serverError.isVisible = true
        binding.serverErrorText.isVisible = true
    }

    private fun setNoInternetState() {
        binding.noConnectionPlaceholder.isVisible = true
        binding.noConnectionText.isVisible = true
    }

    private fun setNoRegionsState() {
        binding.noListPlaceholderGroup.isVisible = true
    }

}
