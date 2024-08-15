package ru.practicum.android.diploma.ui.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceOfWorkBinding

class FilterPlaceOfWorkFragment : Fragment() {

    private val binding: FragmentSelectPlaceOfWorkBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectPlaceOfWorkToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        initButtonListeners()
        initTextBehaviour()
    }

    private fun initButtonListeners() {
        binding.selectPlaceOfWorkToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.applyButton.setOnClickListener { saveAndNavigateUp() }
        // Переход на экран выбора страны
        binding.countryTextInput.setOnClickListener {
            navigateToCountrySelection()
        }
        binding.countryLayout.setOnClickListener {
            navigateToCountrySelection()
        }
        // Переход на экран выбора региона
        binding.regionTextInput.setOnClickListener {
            navigateToRegionSelection()
        }
        binding.regionLayout.setOnClickListener {
            navigateToRegionSelection()
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

    // Метод для обновления внешнего вида hint в зависимости от заполненности поля
    private fun updateCountryHintAppearance(isFilled: Boolean) {
        val isDarkTheme =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

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
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

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

    // Установка состояния страны
    private fun setCountry(country: String?) {
        binding.countryLayout.apply {
            setEndIconDrawable(
                if (country.isNullOrEmpty()) {
                    setEndIconOnClickListener {
                        navigateToCountrySelection()
                    }
                    R.drawable.ic_arrow_forward_14px
                } else {
                    setEndIconOnClickListener {
                        clearCountry()
                    }
                    R.drawable.ic_close_cross_14px
                }
            )
        }
        val countryText = country ?: ""
        binding.countryTextInput.setText(countryText)
    }

    // Установка состояния региона
    private fun setRegion(region: String?) {
        binding.regionLayout.apply {
            setEndIconDrawable(
                if (region.isNullOrEmpty()) {
                    setEndIconOnClickListener {
                        navigateToRegionSelection()
                    }
                    R.drawable.ic_arrow_forward_14px
                } else {
                    setEndIconOnClickListener {
                        clearRegion()
                    }
                    R.drawable.ic_close_cross_14px
                }
            )
        }
        val regionText = region ?: ""
        binding.regionTextInput.setText(regionText)
    }

    // Переход на экран выбора страны
    fun navigateToCountrySelection() {
        // Логика навигации к экрану выбора страны
    }

    // Переход на экран выбора региона
    fun navigateToRegionSelection() {
        // Логика навигации к экрану выбора региона
    }

    // Очистка выбранной страны
    private fun clearCountry() {
        //viewModel.setNewCountry(null)
        clearArguments(0)
    }

    // Очистка выбранного региона
    private fun clearRegion() {
        //viewModel.setNewRegion(null)
        clearArguments(1)
    }

    // Метод для скрытия элементов интерфейса в зависимости от типа
    private fun clearArguments(type: Int) {
        when (type) {
            0 -> binding.countryTextInput.isVisible = false
            1 -> binding.regionTextInput.isVisible = false
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

    // Метод для сохранения настроек
    private fun savePrefs() {
        // Логика сохранения данных, например, через ViewModel
    }

    // Метод для отображения кнопки "Применить"
    private fun applyButtonVisibility(visible: Boolean) {
        binding.applyButton.isVisible = visible
    }

}
