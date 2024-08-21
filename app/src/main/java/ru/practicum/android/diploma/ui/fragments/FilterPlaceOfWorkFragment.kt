package ru.practicum.android.diploma.ui.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
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
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceOfWorkBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.presentation.viewmodels.FilterPlaceOfWorkViewModel
import ru.practicum.android.diploma.ui.fragments.FilterCountryFragment.Companion.COUNTRY_BUNDLE_KEY
import ru.practicum.android.diploma.ui.fragments.FilterCountryFragment.Companion.COUNTRY_REQUEST_KEY
import ru.practicum.android.diploma.ui.fragments.FilterFragment.Companion.FILTER_TO_PLACE_OF_WORK_COUNTRY_KEY
import ru.practicum.android.diploma.ui.fragments.FilterFragment.Companion.FILTER_TO_PLACE_OF_WORK_KEY
import ru.practicum.android.diploma.ui.fragments.FilterFragment.Companion.FILTER_TO_PLACE_OF_WORK_REGION_KEY
import ru.practicum.android.diploma.ui.fragments.FilterRegionFragment.Companion.REGION_REQUEST_KEY
import ru.practicum.android.diploma.ui.state.PlaceOfWorkScreenState

class FilterPlaceOfWorkFragment : Fragment() {
    companion object {
        const val REGION_ID_KEY = "REGION_ID_KEY"
        const val REGION_BUNDLE_KEY = "REGION_BUNDLE_KEY"
        const val PLACE_OF_WORK_KEY = "PLACE_OF_WORK_KEY"
        const val PLACE_OF_WORK_COUNTRY_KEY = "PLACE_OF_WORK_COUNTRY_KEY"
        const val PLACE_OF_WORK_REGION_KEY = "PLACE_OF_WORK_REGION_KEY"
    }

    private val binding: FragmentSelectPlaceOfWorkBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FilterPlaceOfWorkViewModel>()

    private var countryName = ""
    private var regionName = ""
    private var country = Country("", "")
    private var region = Region("", "", null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeViewModel()
    }

    private fun initUI() {
        initButtonListeners()
        initTextBehaviour()
        initResultListeners()
    }

    private fun observeViewModel() {
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaceOfWorkScreenState.CountryName -> handleCountryName(state)
                PlaceOfWorkScreenState.NoCountryName -> handleNoCountryName()
                PlaceOfWorkScreenState.NoRegionName -> handleNoRegionName()
                is PlaceOfWorkScreenState.RegionName -> handleRegionName(state)
                is PlaceOfWorkScreenState.Saved -> handleSavedState(state)
                is PlaceOfWorkScreenState.Loaded -> handleLoadedState(state)
            }
        }
    }

    private fun handleCountryName(state: PlaceOfWorkScreenState.CountryName) {
        country = state.country
        countryName = state.country.name
        binding.countryTextInput.setText(countryName)
        setCountryEndIcon()
        setApplyButtonVisible()
    }

    private fun handleNoCountryName() {
        binding.countryTextInput.text?.clear()
        countryName = ""
        country = Country("", "")
        setNoCountryEndIcon()
        checkFields()
    }

    private fun handleNoRegionName() {
        binding.regionTextInput.text?.clear()
        regionName = ""
        region = Region("", "", null)
        setNoRegionEndIcon()
        checkFields()
    }

    private fun handleRegionName(state: PlaceOfWorkScreenState.RegionName) {
        regionName = state.region.name
        region = state.region
        binding.regionTextInput.setText(regionName)
        setRegionEndIcon()
        setApplyButtonVisible()
    }

    private fun handleSavedState(state: PlaceOfWorkScreenState.Saved) {
        val jsonCountry = Gson().toJson(state.country)
        val jsonRegion = Gson().toJson(state.region)
        setFragmentResult(
            PLACE_OF_WORK_KEY,
            bundleOf(
                PLACE_OF_WORK_COUNTRY_KEY to jsonCountry,
                PLACE_OF_WORK_REGION_KEY to jsonRegion,
            )
        )
        findNavController().navigateUp()
    }

    private fun handleLoadedState(state: PlaceOfWorkScreenState.Loaded) {
        if (state.filters.country != null) {
            country = state.filters.country
            countryName = state.filters.country.name
            binding.countryTextInput.setText(countryName)
            setCountryEndIcon()
        }
        if (state.filters.region != null) {
            region = state.filters.region
            regionName = state.filters.region.name
            binding.regionTextInput.setText(regionName)
            setRegionEndIcon()
        }
        setApplyButtonVisible()
    }

    private fun initResultListeners() {
        setFragmentResultListener(COUNTRY_REQUEST_KEY) { _, bundle ->
            val json = bundle.getString(COUNTRY_BUNDLE_KEY).toString()
            val type = object : TypeToken<Country>() {}.type
            country = Gson().fromJson(json, type)
            viewModel.setCountryName(country)
            setNoRegionEndIcon()
            binding.regionTextInput.text?.clear()
        }

        setFragmentResultListener(REGION_REQUEST_KEY) { _, bundle ->
            val json = bundle.getString(REGION_BUNDLE_KEY).toString()
            val type = object : TypeToken<Region>() {}.type
            region = Gson().fromJson(json, type)
            getCountryName(region)
            viewModel.setRegionName(region)
        }

        setFragmentResultListener(FILTER_TO_PLACE_OF_WORK_KEY) { _, bundle ->
            val json = bundle.getString(FILTER_TO_PLACE_OF_WORK_COUNTRY_KEY).toString()
            val type = object : TypeToken<Country>() {}.type
            country = Gson().fromJson(json, type)
            val jsonRegion = bundle.getString(FILTER_TO_PLACE_OF_WORK_REGION_KEY).toString()
            val typeRegion = object : TypeToken<Region>() {}.type
            region = Gson().fromJson(jsonRegion, typeRegion)
            handleLoadedFilters()
        }
    }

    private fun handleLoadedFilters() {
        if (country.name.isNotEmpty()) {
            countryName = country.name
            binding.countryTextInput.setText(countryName)
            setCountryEndIcon()
            setApplyButtonVisible()
            if (region.name.isNotEmpty()) {
                regionName = region.name
                binding.regionTextInput.setText(regionName)
                setRegionEndIcon()
            }
        }
    }

    private fun initButtonListeners() {
        binding.selectPlaceOfWorkToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener { saveFilters() }
        binding.countryTextInput.setOnClickListener { navigateToCountrySelection() }
        binding.regionTextInput.setOnClickListener { navigateToRegionSelection() }
    }

    private fun getCountryName(region: Region) {
        viewModel.getCountryName(region, false)
    }

    private fun initTextBehaviour() {
        binding.countryTextInput.doOnTextChanged { text, _, _, _ ->
            updateCountryHintAppearance(text?.isNotEmpty() == true)
        }

        binding.regionTextInput.doOnTextChanged { text, _, _, _ ->
            updateRegionHintAppearance(text?.isNotEmpty() == true)
        }
    }

    private fun saveFilters() {
        viewModel.saveFields(country, region)
    }

    private fun setNoCountryEndIcon() {
        binding.countryLayout.apply {
            setEndIconDrawable(R.drawable.ic_arrow_forward_14px)
            setEndIconOnClickListener { navigateToCountrySelection() }
        }
    }

    private fun setCountryEndIcon() {
        binding.countryLayout.apply {
            setEndIconDrawable(R.drawable.ic_close_cross_14px)
            setEndIconOnClickListener { viewModel.setCountryName(Country("", "")) }
        }
    }

    private fun setNoRegionEndIcon() {
        binding.regionLayout.apply {
            setEndIconDrawable(R.drawable.ic_arrow_forward_14px)
            setEndIconOnClickListener { navigateToRegionSelection() }
        }
    }

    private fun setRegionEndIcon() {
        binding.regionLayout.apply {
            setEndIconDrawable(R.drawable.ic_close_cross_14px)
            setEndIconOnClickListener { viewModel.setRegionName(Region("", "", null)) }
        }
    }

    private fun checkFields() {
        binding.applyButton.isVisible = binding.countryTextInput.text?.isNotEmpty() == true ||
            binding.regionTextInput.text?.isNotEmpty() == true
    }

    private fun setApplyButtonVisible() {
        binding.applyButton.isVisible = true
    }

    private fun updateCountryHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isFilled) {
            binding.countryLayout.setHintTextAppearance(R.style.text_12_regular_400)
            binding.countryLayout.defaultHintTextColor = ColorStateList.valueOf(
                if (isDarkTheme) requireContext().getColor(R.color.white) else requireContext().getColor(R.color.black)
            )
        } else {
            binding.countryLayout.setHintTextAppearance(R.style.text_16_regular_400)
            binding.countryLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.gray))
        }
    }

    private fun updateRegionHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isFilled) {
            binding.regionLayout.setHintTextAppearance(R.style.text_12_regular_400)
            binding.regionLayout.defaultHintTextColor = ColorStateList.valueOf(
                if (isDarkTheme) requireContext().getColor(R.color.white) else requireContext().getColor(R.color.black)
            )
        } else {
            binding.regionLayout.setHintTextAppearance(R.style.text_16_regular_400)
            binding.regionLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.gray))
        }
    }

    private fun navigateToCountrySelection() {
        findNavController().navigate(R.id.action_selectPlaceOfWorkFragment_to_filtersCountryFragment)
    }

    private fun navigateToRegionSelection() {
        setFragmentResult(REGION_ID_KEY, bundleOf(REGION_BUNDLE_KEY to country.id))
        findNavController().navigate(
            R.id.action_selectPlaceOfWorkFragment_to_filterRegionFragment
        )
    }
}

