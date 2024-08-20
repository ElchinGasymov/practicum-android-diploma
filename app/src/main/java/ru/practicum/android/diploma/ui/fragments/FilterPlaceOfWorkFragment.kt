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
import ru.practicum.android.diploma.ui.fragments.FilterFragment.Companion.FILTER_TO_PLACE_OF_WORK_KEY
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

        initButtonListeners()
        initTextBehaviour()
        initResultListeners()

        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaceOfWorkScreenState.CountryName -> {
                    country = state.country
                    countryName = state.country.name
                    binding.countryTextInput.setText(countryName)
                    setCountryEndIcon()
                    setApplyButtonVisible()
                }

                PlaceOfWorkScreenState.NoCountryName -> {
                    binding.countryTextInput.text?.clear()
                    countryName = ""
                    country = Country("", "")
                    setNoCountryEndIcon()
                    checkFields()
                }

                PlaceOfWorkScreenState.NoRegionName -> {
                    binding.regionTextInput.text?.clear()
                    regionName = ""
                    region = Region("", "", null)
                    setNoRegionEndIcon()
                    checkFields()
                }

                is PlaceOfWorkScreenState.RegionName -> {
                    regionName = state.region.name
                    region = state.region
                    binding.regionTextInput.setText(regionName)
                    setRegionEndIcon()
                    setApplyButtonVisible()
                }

                is PlaceOfWorkScreenState.Saved -> {
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

                is PlaceOfWorkScreenState.Loaded -> {
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
            }

        }
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
        setFragmentResultListener(FILTER_TO_PLACE_OF_WORK_KEY) { _, _ ->
            viewModel.getFilterSetting()
        }
    }

    private fun initButtonListeners() {
        binding.selectPlaceOfWorkToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener { saveFilters() }

        binding.countryTextInput.setOnClickListener {
            navigateToCountrySelection()
        }
        binding.regionTextInput.setOnClickListener {
            navigateToRegionSelection()
        }
    }

    private fun getCountryName(region: Region) {
        viewModel.getCountryName(region, false)
    }

    private fun initTextBehaviour() {
        // Настройка поведения поля countryTextInput при изменении текста
        binding.countryTextInput.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                updateCountryHintAppearance(false) // Если текст пустой, hint остаётся стандартным
            } else {
                updateCountryHintAppearance(true) // Если текст заполнен, hint уменьшается и меняет цвет
            }
        }

        // Настройка поведения поля regionTextInput при изменении текста
        binding.regionTextInput.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                updateRegionHintAppearance(false)
            } else {
                updateRegionHintAppearance(true)
            }
        }
    }

    private fun saveFilters() {
       // viewModel.saveSharedPrefs(country, region)
        viewModel.saveFields(country, region)
    }

    private fun setNoCountryEndIcon() {
        binding.countryLayout.apply {
            setEndIconDrawable(R.drawable.ic_arrow_forward_14px)
            setEndIconOnClickListener {
                navigateToCountrySelection()
            }
        }
    }

    private fun setCountryEndIcon() {
        binding.countryLayout.apply {
            setEndIconDrawable(R.drawable.ic_close_cross_14px)
            setEndIconOnClickListener {
                viewModel.setCountryName(Country("", ""))
            }
        }
    }

    private fun setNoRegionEndIcon() {
        binding.regionLayout.apply {
            setEndIconDrawable(R.drawable.ic_arrow_forward_14px)
            setEndIconOnClickListener {
                navigateToRegionSelection()
            }
        }
    }

    private fun setRegionEndIcon() {
        binding.regionLayout.apply {
            setEndIconDrawable(R.drawable.ic_close_cross_14px)
            setEndIconOnClickListener {
                viewModel.setRegionName(
                    Region("", "", null)
                )
            }
        }
    }

    private fun checkFields() {
        binding.applyButton.isVisible = binding.countryTextInput.text?.isNotEmpty() == true ||
            binding.regionTextInput.text?.isNotEmpty() == true
    }

    private fun setApplyButtonVisible() {
        binding.applyButton.isVisible = true
    }

    // Метод для обновления внешнего вида hint в зависимости от заполненности поля
    private fun updateCountryHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isFilled) {
            // Если поле заполнено, уменьшаем hint и меняем его цвет в зависимости от темы
            binding.countryLayout.setHintTextAppearance(R.style.text_12_regular_400)
            binding.countryLayout.defaultHintTextColor = ColorStateList.valueOf(
                if (isDarkTheme) requireContext().getColor(R.color.white) else requireContext().getColor(R.color.black)
            )
        } else {
            // Если поле пустое, оставляем стандартный размер и серый цвет hint
            binding.countryLayout.setHintTextAppearance(R.style.text_16_regular_400)
            binding.countryLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.gray))
        }
    }

    // Метод для обновления внешнего вида hint в зависимости от заполненности поля regionTextInput
    private fun updateRegionHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isFilled) {
            // Если поле заполнено, уменьшаем hint и меняем его цвет в зависимости от темы
            binding.regionLayout.setHintTextAppearance(R.style.text_12_regular_400)
            binding.regionLayout.defaultHintTextColor = ColorStateList.valueOf(
                if (isDarkTheme) requireContext().getColor(R.color.white) else requireContext().getColor(R.color.black)
            )
        } else {
            // Если поле пустое, оставляем стандартный размер и серый цвет hint
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
