package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.viewmodels.FilterCountryViewModel
import ru.practicum.android.diploma.ui.state.CountriesScreenState
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.adapter.country.CountryAdapter

class FilterCountryFragment : Fragment() {

    companion object {
        const val COUNTRY_REQUEST_KEY = "COUNTRY_REQUEST_KEY"
        const val COUNTRY_NAME_KEY = "COUNTRY_BUNDLE_KEY"
        const val COUNTRY_ID_BUNDLE_KEY = "COUNTRY_ID_BUNDLE_KEY"
    }

    private val binding: FragmentSelectCountryBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FilterCountryViewModel>()

    private val adapter = CountryAdapter {
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

        binding.selectCountryToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                CountriesScreenState.Default -> {}
                is CountriesScreenState.Error -> {
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
                            setNoListState()
                        }
                    }
                }

                CountriesScreenState.Loading -> {
                    removePlaceholders()
                    startProgressBar()
                }

                is CountriesScreenState.Success -> {
                    stopProgressBar()
                    adapter.setCountries(state.regions)
                }
            }

        }
        val countriesRecyclerView = binding.countryRecycleView
        countriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        countriesRecyclerView.adapter = adapter
        viewModel.getCountries()

    }

    private fun onItemClicked(country: Country) {
        setFragmentResult(COUNTRY_REQUEST_KEY, bundleOf(
            COUNTRY_NAME_KEY to country.name,
            COUNTRY_ID_BUNDLE_KEY to country.id))
        findNavController().navigateUp()
    }

    private fun startProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun stopProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun removePlaceholders() {
        binding.placeholderGroup.isVisible = false
        binding.noConnectionPlaceholder.isVisible = false
        binding.noConnectionText.isVisible = false
        binding.serverError.isVisible = false
        binding.serverErrorText.isVisible = false
    }

    private fun setNoInternetState() {
        binding.noConnectionPlaceholder.isVisible = true
        binding.noConnectionText.isVisible = true
    }

    private fun setServerErrorState() {
        binding.serverError.isVisible = true
        binding.serverErrorText.isVisible = true
    }

    private fun setNoListState() {
        binding.placeholderGroup.isVisible = true
    }

}
