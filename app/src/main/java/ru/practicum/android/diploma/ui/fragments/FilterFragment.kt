package ru.practicum.android.diploma.ui.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private val binding: FragmentFilterBinding by viewBinding(CreateMethod.INFLATE)

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
    }

    private fun initButtonListeners() {
        binding.filterSettingsTitle.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener { saveAndNavigateUp() }
        binding.clearButton.setOnClickListener { clearFilters() }
        binding.salaryFlagCheckbox.setOnCheckedChangeListener { _, isChecked -> // Пример использования Checkbox, если включена опция показа только с зарплатой
            //viewModel.setSalaryOnlyCheckbox(isChecked)
        }
        // Переход на экран выбора индустрии
        binding.industryTextInput.setOnClickListener {
            navigateToIndustry()
        }
        binding.industryLayout.setOnClickListener {
            navigateToIndustry()
        }
        // Переход на экран выбора места работы
        binding.workPlaceLayout.setOnClickListener {
            navigateToPlaceOfWork()
        }
        binding.workTextInput.setOnClickListener {
            navigateToPlaceOfWork()
        }
    }

    private fun initTextBehaviour() {
        // Настройка поведения поля salary при изменении текста
        binding.salary.doOnTextChanged { text, _, _, _ ->
            //viewModel.setExpectedSalary(text?.toString())
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
                    binding.salary.setText("")
                }
            }
        }

        // ОНастройка поведения поля workTextInput при изменении текста
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
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

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
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

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

    // Установка состояния местоположения (страны и региона)
    private fun setStateLocation(country: String?, region: String?) {
        binding.workPlaceLayout.apply {
            setEndIconDrawable(
                if (country.isNullOrEmpty()) {
                    setEndIconOnClickListener {
                        navigateToPlaceOfWork()
                    }
                    R.drawable.ic_arrow_forward_14px
                } else {
                    setEndIconOnClickListener {
                        clearWorkPlace()
                    }
                    R.drawable.ic_close_cross_14px
                }
            )
        }
        // Формирование текста для отображения страны и региона
        val place = country + if (!region.isNullOrEmpty()) {
            getString(R.string.separator) + " $region"
        } else {
            ""
        }
        binding.workTextInput.setText(place)
    }

    // Установка состояния индустрии (напр. выбранная индустрия)
    private fun setStateIndustry(industry: String?) {
        binding.industryLayout.apply {
            setEndIconDrawable(
                if (industry.isNullOrEmpty()) {
                    setOnClickListener {
                        navigateToIndustry()
                    }
                    R.drawable.ic_arrow_forward_14px
                } else {
                    setEndIconOnClickListener {
                        clearIndustry()
                    }
                    R.drawable.ic_close_cross_14px
                }
            )
        }
        binding.industryTextInput.setText(industry)
    }

    // Переход на экран выбора места работы
    fun navigateToPlaceOfWork() {

    }

    // Переход на экран выбора индустрии
    fun navigateToIndustry() {

    }

    // Очистка выбранного места работы
    private fun clearWorkPlace() {
        //viewModel.setNewRegionCountry(region = null, country = null, countryId = null, regionId = null)
        clearArguments(1)
    }

    // Очистка выбранной индустрии
    private fun clearIndustry() {
        //viewModel.setNewIndustry(null)
        clearArguments(0)
    }

    // Очистка всех фильтров
    private fun clearFilters() {
        binding.apply {
            clearButton.isVisible = false
            salary.setText("")
        }
        //viewModel.clearPrefs()
    }

    // Метод для скрытия элементов интерфейса в зависимости от типа
    private fun clearArguments(type: Int) {
        when (type) {
            0 -> binding.industryTextInput.isVisible = false
            1 -> binding.workTextInput.isVisible = false
        }
    }

    // Сохранение настроек и возврат к предыдущему экрану
    private fun saveAndNavigateUp() {
        lifecycleScope.launch(Dispatchers.IO) {
            savePrefs()
            withContext(Dispatchers.Main) {
                findNavController().navigateUp()
            }
        }
    }

    // Метод для сохранения настроек фильтров
    private fun savePrefs() {
        //viewModel.savePrefs()
    }

    // Метод для отображения кнопки "Применить"
    private fun applyButtonVisibility(visible: Boolean) {
        binding.applyButton.isVisible = visible
    }

    // Метод для отображения кнопки "Сбросить"
    private fun clearButtonVisibility(visible: Boolean) {
        binding.clearButton.isVisible = visible
    }
}
