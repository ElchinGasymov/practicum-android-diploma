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
import ru.practicum.android.diploma.ui.state.PlaceOfWorkScreenState
import ru.practicum.android.diploma.util.COUNTRY_BUNDLE_KEY
import ru.practicum.android.diploma.util.COUNTRY_REQUEST_KEY
import ru.practicum.android.diploma.util.FILTER_TO_PLACE_OF_WORK_COUNTRY_KEY
import ru.practicum.android.diploma.util.FILTER_TO_PLACE_OF_WORK_KEY
import ru.practicum.android.diploma.util.FILTER_TO_PLACE_OF_WORK_REGION_KEY
import ru.practicum.android.diploma.util.PLACE_OF_WORK_COUNTRY_KEY
import ru.practicum.android.diploma.util.PLACE_OF_WORK_KEY
import ru.practicum.android.diploma.util.PLACE_OF_WORK_REGION_KEY
import ru.practicum.android.diploma.util.REGION_BUNDLE_KEY
import ru.practicum.android.diploma.util.REGION_ID_KEY
import ru.practicum.android.diploma.util.REGION_REQUEST_KEY

class FilterPlaceOfWorkFragment : Fragment() {
    private val binding: FragmentSelectPlaceOfWorkBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FilterPlaceOfWorkViewModel>()
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
        binding.countryTextInput.setText(state.country.name)
        setCountryEndIcon()
        setApplyButtonVisible()
    }

    private fun handleNoCountryName() {
        binding.countryTextInput.text?.clear()
        country = Country("", "")
        setNoCountryEndIcon()
        checkFields()
    }

    private fun handleNoRegionName() {
        binding.regionTextInput.text?.clear()
        region = Region("", "", null)
        setNoRegionEndIcon()
        checkFields()
    }

    private fun handleRegionName(state: PlaceOfWorkScreenState.RegionName) {
        region = state.region
        binding.regionTextInput.setText(state.region.name)
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
            binding.countryTextInput.setText(state.filters.country.name)
            setCountryEndIcon()
        }
        if (state.filters.region != null) {
            region = state.filters.region
            binding.regionTextInput.setText(state.filters.region.name)
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
            viewModel.getCountryName(region, false)
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
            binding.countryTextInput.setText(country.name)
            setCountryEndIcon()
            setApplyButtonVisible()
            if (region.name.isNotEmpty()) {
                binding.regionTextInput.setText(region.name)
                setRegionEndIcon()
            }
        }
    }

    private fun initButtonListeners() {
        binding.selectPlaceOfWorkToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener { viewModel.saveFields(country, region) }
        binding.countryTextInput.setOnClickListener { navigateToCountrySelection() }
        binding.regionTextInput.setOnClickListener { navigateToRegionSelection() }
    }

    private fun initTextBehaviour() {
        binding.countryTextInput.doOnTextChanged { text, _, _, _ ->
            updateCountryHintAppearance(text?.isNotEmpty() == true)
        }
        binding.regionTextInput.doOnTextChanged { text, _, _, _ ->
            updateRegionHintAppearance(text?.isNotEmpty() == true)
        }
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

