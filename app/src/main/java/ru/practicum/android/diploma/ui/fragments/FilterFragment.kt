package ru.practicum.android.diploma.ui.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
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
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs
import ru.practicum.android.diploma.presentation.viewmodels.FilterViewModel
import ru.practicum.android.diploma.ui.fragments.FilterIndustryFragment.Companion.INDUSTRY_ITEM_KEY
import ru.practicum.android.diploma.ui.fragments.FilterIndustryFragment.Companion.INDUSTRY_KEY
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.PLACE_OF_WORK_COUNTRY_KEY
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.PLACE_OF_WORK_KEY
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.PLACE_OF_WORK_REGION_KEY
import ru.practicum.android.diploma.ui.state.FilterScreenState

class FilterFragment : Fragment() {

    companion object {
        const val FILTER_REQUEST_KEY = "FILTER_REQUEST_KEY"
        const val FILTER_BUNDLE_KEY = "FILTER_BUNDLE_KEY"
        const val FILTER_TO_PLACE_OF_WORK_KEY = "FILTER_TO_PLACE_OF_WORK_KEY"
    }

    private val binding: FragmentFilterBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FilterViewModel>()
    private var init = true
    private var region = Region("", "", null)
    private var country = Country("", "")
    private var industries = Industries("", "", false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonListeners() // Инициализация слушателей для кнопок и других элементов интерфейса
        initTextBehaviour() // Инициализация поведения текстовых полей
        initResultListeners()
        binding.salary.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.btnGroup.isVisible = true
                viewModel.saveFilter(makeFilterSettings())
            } else {
                checkFields()
            }
        }
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FilterScreenState.PlaceOfWork -> {
                    binding.workTextInput.setText(state.countryName)
                    viewModel.saveFilter(makeFilterSettings())
                    setCountryEndIcon()
                    setButtonsVisible()
                }

                FilterScreenState.ClearState -> {
                    binding.workTextInput.text?.clear()
                    binding.industryTextInput.text?.clear()
                    binding.salary.text?.clear()
                    setNoIndustryEndIcon()
                    setNoCountryEndIcon()
                    setButtonsNotVisible()
                    setFragmentResult(FILTER_REQUEST_KEY, bundleOf())
                }

                is FilterScreenState.Industry -> {
                    binding.industryTextInput.setText(state.industry)
                    setIndustryEndIcon()
                }

                FilterScreenState.NoIndustry -> {
                    binding.industryTextInput.text?.clear()
                    setNoIndustryEndIcon()
                    checkFields()
                    industries = Industries("", "", false)
                }

                FilterScreenState.NoPlaceOfWork -> {
                    binding.workTextInput.text?.clear()
                    setNoCountryEndIcon()
                    checkFields()
                    country = Country("", "")
                    region = Region("", "", null)
                }

                is FilterScreenState.FiltersSaved -> {
                    val json = Gson().toJson(state.filters)
                    setFragmentResult(FILTER_REQUEST_KEY, bundleOf(FILTER_BUNDLE_KEY to json))
                    findNavController().navigateUp()
                }

                is FilterScreenState.FiltersLoaded -> {
                    binding.workTextInput.setText(
                        setPlaceOfWorkName(
                            state.filters.country?.name.toString(),
                            state.filters.region?.name.toString()
                        )
                    )
                    binding.industryTextInput.setText(state.filters.industries?.name)
                    binding.salary.setText(state.filters.currency)
                    binding.salaryFlagCheckbox.isChecked = state.filters.noCurrency
                }
            }
        }
        viewModel.getFilterSetting()
    }

    private fun checkFields() {
        if (binding.workTextInput.text?.isNotEmpty() == true ||
            binding.industryTextInput.text?.isNotEmpty() == true ||
            binding.salary.text?.isNotEmpty() == true
        ) {
            setButtonsVisible()
        } else {
            setButtonsNotVisible()
        }
    }

    private fun setButtonsVisible() {
        binding.btnGroup.isVisible = true
    }

    private fun setButtonsNotVisible() {
        binding.btnGroup.isVisible = false
    }

    private fun initResultListeners() {
        setFragmentResultListener(PLACE_OF_WORK_KEY) { _, bundle ->

            val countryJson = bundle.getString(PLACE_OF_WORK_COUNTRY_KEY).toString()
            val type = object : TypeToken<Country>() {}.type
            country = Gson().fromJson(countryJson, type)

            val regionJson = bundle.getString(PLACE_OF_WORK_REGION_KEY)
            val typeRegion = object : TypeToken<Region>() {}.type
            region = Gson().fromJson(regionJson, typeRegion)
            val countryName = setPlaceOfWorkName(country.name, region.name)
            viewModel.setPlaceOfWork(countryName)
        }
        setFragmentResultListener(INDUSTRY_KEY) { _, bundle ->
            val industryJson = bundle.getString(INDUSTRY_ITEM_KEY).toString()
            val type = object : TypeToken<Industries>() {}.type
            val industry = Gson().fromJson<Industries>(industryJson, type)
            industries = industry
            viewModel.setIndustry(industry.name)
            viewModel.setIndustrySelected(industry)
        }
    }

    private fun setPlaceOfWorkName(countryName: String, regionName: String): String {
        val placeOfWork = StringBuilder()
        if (countryName.isNotEmpty()) {
            placeOfWork.append(countryName)
            if (regionName.isNotEmpty()) {
                placeOfWork.append(", ").append(regionName)
            }
        }
        return placeOfWork.toString()
    }

    private fun initButtonListeners() {
        binding.filterSettingsTitle.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener { saveFilterSettings() }
        binding.clearButton.setOnClickListener { viewModel.clear() }
        // Пример использования Checkbox, если включена опция показа только с зарплатой
        binding.salaryFlagCheckbox.setOnCheckedChangeListener { _, isChecked -> //
            viewModel.saveFilter(makeFilterSettings())
            viewModel.setNoCurrencySelected(isChecked)
            // viewModel.setSalaryOnlyCheckbox(isChecked)
        }
        binding.workTextInput.setOnClickListener {
            navigateToPlaceOfWorkScreen()
        }
        binding.industryTextInput.setOnClickListener {
            navigateToIndustryScreen()
        }
    }

    private fun initTextBehaviour() {
        //   initSalaryTextBehaviour()
        initWorkAndIndustryTextBehaviour()
    }

    private fun saveFilterSettings() {
        viewModel.saveFilterAndClose(makeFilterSettings())
    }

    private fun makeFilterSettings(): SaveFiltersSharedPrefs {
        val filter = SaveFiltersSharedPrefs(
            industries,
            country,
            region,
            binding.salary.text.toString(),
            binding.salaryFlagCheckbox.isChecked
        )
        return filter
    }

    // Настройка поведения поля salary при изменении текста
    /* private fun initSalaryTextBehaviour() {
         binding.salary.doOnTextChanged { text, _, _, _ ->
             // viewModel.setExpectedSalary(text?.toString())
             if (text.isNullOrEmpty()) {
                 // Если текст пустой, убираем иконку и меняем цвет hint
                 binding.salaryLayout.endIconMode = TextInputLayout.END_ICON_NONE
                 binding.salaryLayout.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
                 if (binding.salary.isFocused) {
                     binding.salaryLayout.defaultHintTextColor =
                         ColorStateList.valueOf(requireContext().getColor(R.color.blue))
                 } else {
                     binding.salaryLayout.defaultHintTextColor =
                         ColorStateList.valueOf(requireContext().getColorOnSecondaryFixed())
                 }
             } else {
                 // Если текст заполнен, показываем иконку и меняем цвет hint
                 if (binding.salary.isFocused) {
                     binding.salaryLayout.defaultHintTextColor =
                         ColorStateList.valueOf(requireContext().getColor(R.color.blue))
                 } else {
                     binding.salaryLayout.defaultHintTextColor =
                         ColorStateList.valueOf(requireContext().getColor(R.color.black))
                 }
                 binding.salaryLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
                 binding.salaryLayout.setEndIconOnClickListener {
                     binding.salary.text?.clear()
                 }
             }
         }
     }*/

    // Настройка поведения полей workTextInput и industryTextInput при изменении текста
    private fun initWorkAndIndustryTextBehaviour() {
        // Настройка поведения поля workTextInput при изменении текста
        binding.workTextInput.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                updateWorkPlaceHintAppearance(false) // Если текст пустой, hint остаётся стандартным
            } else {
                updateWorkPlaceHintAppearance(true) // Если текст заполнен, hint уменьшается и меняет цвет
            }
        }

        // Настройка поведения поля industryTextInput при изменении текста
        binding.industryTextInput.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                updateIndustryHintAppearance(false)
            } else {
                updateIndustryHintAppearance(true)
            }
        }
    }

    // Метод для обновления внешнего вида hint в зависимости от заполненности поля
    private fun updateWorkPlaceHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isFilled) {
            // Если поле заполнено, уменьшаем hint и меняем его цвет в зависимости от темы
            binding.workPlaceLayout.setHintTextAppearance(R.style.text_12_regular_400)
            binding.workPlaceLayout.defaultHintTextColor = ColorStateList.valueOf(
                if (isDarkTheme) requireContext().getColor(R.color.white) else requireContext().getColor(R.color.black)
            )
        } else {
            // Если поле пустое, оставляем стандартный размер и серый цвет hint
            binding.workPlaceLayout.setHintTextAppearance(R.style.text_16_regular_400)
            binding.workPlaceLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.gray))
        }
    }

    // Метод для обновления внешнего вида hint в зависимости от заполненности поля industryTextInput
    private fun updateIndustryHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isFilled) {
            // Если поле заполнено, уменьшаем hint и меняем его цвет в зависимости от темы
            binding.industryLayout.setHintTextAppearance(R.style.text_12_regular_400)
            binding.industryLayout.defaultHintTextColor = ColorStateList.valueOf(
                if (isDarkTheme) requireContext().getColor(R.color.white) else requireContext().getColor(R.color.black)
            )
        } else {
            // Если поле пустое, оставляем стандартный размер и серый цвет hint
            binding.industryLayout.setHintTextAppearance(R.style.text_16_regular_400)
            binding.industryLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.gray))
        }
    }

    // Метод для получения цвета в зависимости от текущей темы приложения
    @ColorInt
    fun Context.getColorOnSecondaryFixed(): Int {
        val typedValue = TypedValue()
        val theme = theme
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondaryFixed, typedValue, true)
        return typedValue.data
    }

    private fun setNoCountryEndIcon() {
        binding.workPlaceLayout.apply {
            setEndIconDrawable(R.drawable.ic_arrow_forward_14px)
            setEndIconOnClickListener {
                navigateToPlaceOfWorkScreen()
            }
        }
    }

    private fun setCountryEndIcon() {
        binding.workPlaceLayout.apply {
            setEndIconDrawable(R.drawable.ic_close_cross_14px)
            setEndIconOnClickListener {
                viewModel.setPlaceOfWork("")
            }
        }
    }

    private fun setNoIndustryEndIcon() {
        binding.industryLayout.apply {
            setEndIconDrawable(R.drawable.ic_arrow_forward_14px)
            setEndIconOnClickListener {
                navigateToIndustryScreen()
            }
        }
    }

    private fun setIndustryEndIcon() {
        binding.industryLayout.apply {
            setEndIconDrawable(R.drawable.ic_close_cross_14px)
            setEndIconOnClickListener {
                viewModel.setIndustry("")
            }
        }
    }

    private fun navigateToPlaceOfWorkScreen() {
        setFragmentResult(FILTER_TO_PLACE_OF_WORK_KEY, bundleOf())
        findNavController().navigate(R.id.action_filterFragment_to_select_place_of_workFragment)
    }

    private fun navigateToIndustryScreen() {
        findNavController().navigate(R.id.action_filterFragment_to_select_industryFragment)
    }

    // Метод для скрытия элементов интерфейса в зависимости от типа
    // private fun clearArguments(type: Int) {
    //     when (type) {
    //         0 -> binding.industryTextInput.isVisible = false
    //         1 -> binding.workTextInput.isVisible = false
    //     }
    // }

}
