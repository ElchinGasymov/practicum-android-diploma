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
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceOfWorkBinding
import ru.practicum.android.diploma.presentation.viewmodels.FilterPlaceOfWorkViewModel
import ru.practicum.android.diploma.ui.fragments.FilterCountryFragment.Companion.COUNTRY_ID_BUNDLE_KEY
import ru.practicum.android.diploma.ui.fragments.FilterCountryFragment.Companion.COUNTRY_NAME_KEY
import ru.practicum.android.diploma.ui.fragments.FilterCountryFragment.Companion.COUNTRY_REQUEST_KEY
import ru.practicum.android.diploma.ui.fragments.FilterRegionFragment.Companion.REGION_NAME_KEY
import ru.practicum.android.diploma.ui.fragments.FilterRegionFragment.Companion.REGION_PARENT_ID_KEY
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
    private var countryId = ""


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
                    countryName = state.countryName
                    binding.countryTextInput.setText(countryName)
                    setCountryEndIcon()
                    setApplyButtonVisible()
                }

                PlaceOfWorkScreenState.NoCountryName -> {
                    binding.countryTextInput.text?.clear()
                    countryName = ""
                    setNoCountryEndIcon()
                    checkFields()
                }

                PlaceOfWorkScreenState.NoRegionName -> {
                    binding.regionTextInput.text?.clear()
                    regionName = ""
                    setNoRegionEndIcon()
                    checkFields()
                }

                is PlaceOfWorkScreenState.RegionName -> {
                    regionName = state.regionName
                    binding.regionTextInput.setText(regionName)
                    setRegionEndIcon()
                    setApplyButtonVisible()
                }

                is PlaceOfWorkScreenState.Saved -> {
                    setFragmentResult(
                        PLACE_OF_WORK_KEY, bundleOf(
                            PLACE_OF_WORK_COUNTRY_KEY to state.countryName,
                            PLACE_OF_WORK_REGION_KEY to binding.regionTextInput.text.toString()
                        )
                    )
                    findNavController().navigateUp()
                }
            }

        }
    }

    private fun initResultListeners() {
        setFragmentResultListener(COUNTRY_REQUEST_KEY) { _, bundle ->
            viewModel.setCountryName(bundle.getString(COUNTRY_NAME_KEY).toString())
            countryId = bundle.getString(COUNTRY_ID_BUNDLE_KEY).toString()
        }

        setFragmentResultListener(REGION_REQUEST_KEY) { _, bundle ->
            viewModel.setRegionName(bundle.getString(REGION_NAME_KEY).toString())
            getCountryName(bundle.getInt(REGION_PARENT_ID_KEY).toString())
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

    private fun getCountryName(countryId: String) {
        if (countryName.isEmpty()) {
            this.countryId = countryId
            viewModel.getCountryName(countryId, false)
        }
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
        viewModel.saveFields(binding.countryTextInput.text.toString(), countryId)
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
                viewModel.setCountryName("")
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
                viewModel.setRegionName("")
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
        setFragmentResult(REGION_ID_KEY, bundleOf(REGION_BUNDLE_KEY to countryId))
        findNavController().navigate(
            R.id.action_selectPlaceOfWorkFragment_to_filterRegionFragment
        )
    }

}
