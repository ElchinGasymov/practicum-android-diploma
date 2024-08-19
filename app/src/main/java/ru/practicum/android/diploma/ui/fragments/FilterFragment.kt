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
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
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
import ru.practicum.android.diploma.presentation.viewmodels.FilterViewModel
import ru.practicum.android.diploma.ui.fragments.FilterIndustryFragment.Companion.INDUSTRY_ITEM_KEY
import ru.practicum.android.diploma.ui.fragments.FilterIndustryFragment.Companion.INDUSTRY_KEY
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.PLACE_OF_WORK_COUNTRY_KEY
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.PLACE_OF_WORK_KEY
import ru.practicum.android.diploma.ui.fragments.FilterPlaceOfWorkFragment.Companion.PLACE_OF_WORK_REGION_KEY
import ru.practicum.android.diploma.ui.state.FilterScreenState

class FilterFragment : Fragment() {

    private val binding: FragmentFilterBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FilterViewModel>()

    private var regionId = ""
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
                viewModel.setCurrencySelected(text.toString().toInt())
            } else {
                checkFields()
            }
        }
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FilterScreenState.PlaceOfWork -> {
                    binding.workTextInput.setText(state.countryName)
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
                    viewModel.clearSharedPrefs()
                }

                is FilterScreenState.Industry -> {
                    binding.industryTextInput.setText(state.industry)
                    setIndustryEndIcon()
                }

                FilterScreenState.NoIndustry -> {
                    binding.industryTextInput.text?.clear()
                    setNoIndustryEndIcon()
                    checkFields()
                }

                FilterScreenState.NoPlaceOfWork -> {
                    binding.workTextInput.text?.clear()
                    setNoCountryEndIcon()
                    checkFields()
                }
            }
        }


        viewModel.readSharedPrefs()
        if (true) {
            viewModel.sharedPrefs.observe(viewLifecycleOwner) { filters ->
                if (filters.industries != null) {
                    viewModel.setIndustry(filters.industries.name)
                    viewModel.setIndustrySelected(filters.industries)
                }

                if (filters.noCurrency != null) {
                    binding.salaryFlagCheckbox.isChecked = filters.noCurrency
                    viewModel.setNoCurrencySelected(filters.noCurrency)
                }

                if (filters.currency != null) {
                    binding.salary.setText(filters.currency.toString())
                    viewModel.setCurrencySelected(filters.currency)
                }

                if (filters.country != null) {
                    val placeOfWork = StringBuilder()
                    placeOfWork.append(filters.country.name)
                    if (filters.region != null) {
                        placeOfWork.append(", ").append(filters.region.name)
                        viewModel.setRegionSelected(filters.region)
                    }
                    viewModel.setPlaceOfWork(placeOfWork.toString())
                    viewModel.setCountrySelected(filters.country)
                }

            }

        }
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
            val country = Gson().fromJson<Country>(countryJson, type)

            val regionJson = bundle.getString(PLACE_OF_WORK_REGION_KEY)
            val typeRegion = object : TypeToken<Region>() {}.type
            val region = Gson().fromJson<Region>(regionJson, typeRegion)

            regionId = region.id
            val placeOfWork = StringBuilder()
            placeOfWork.append(country.name)
            if (region.name.isNotEmpty()) {
                placeOfWork.append(", ").append(region.name)
                viewModel.setRegionSelected(region)
            }
            viewModel.setPlaceOfWork(placeOfWork.toString())
            viewModel.setCountrySelected(country)
        }

        setFragmentResultListener(INDUSTRY_KEY) { _, bundle ->
            val industryJson = bundle.getString(INDUSTRY_ITEM_KEY).toString()
            val type = object : TypeToken<Industries>() {}.type
            val industry = Gson().fromJson<Industries>(industryJson, type)
            viewModel.setIndustry(industry.name.toString())
            viewModel.setIndustrySelected(industry)
        }
    }

    private fun initButtonListeners() {
        binding.filterSettingsTitle.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener {
            viewModel.saveFilter()
            findNavController().popBackStack()
        }
        binding.clearButton.setOnClickListener { viewModel.clear() }
        // Пример использования Checkbox, если включена опция показа только с зарплатой
        binding.salaryFlagCheckbox.setOnCheckedChangeListener { _, isChecked -> //
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
