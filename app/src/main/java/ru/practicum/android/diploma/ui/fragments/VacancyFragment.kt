package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.ui.fragments.SearchFragment.Companion.VACANCY_KEY
import ru.practicum.android.diploma.ui.state.FavouriteState
import ru.practicum.android.diploma.ui.state.VacancyScreenState
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.adapter.converterSalaryToString


class VacancyFragment : Fragment() {

    private val binding: FragmentVacancyBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<VacancyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            viewModel.getVacancyDetails(it.getString(VACANCY_KEY).toString())
        }
        binding.vacancyToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.favouriteRender().observe(viewLifecycleOwner) { state ->
            when (state) {
                FavouriteState.Favourite -> {
                    binding.vacancyFavoriteIcon.setImageResource(R.drawable.ic_favorites_on_20px)
                }

                FavouriteState.NotFavourite -> {
                    binding.vacancyFavoriteIcon.setImageResource(R.drawable.ic_favorites_off_23px)
                }
            }
        }

        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                VacancyScreenState.Default -> {}
                is VacancyScreenState.Error -> {
                    when (state.error) {
                        ResponseData.ResponseError.NO_INTERNET -> {
                            hideVacancy()
                            binding.noInternetPlaceholderGroup.isVisible = true
                        }

                        ResponseData.ResponseError.CLIENT_ERROR,
                        ResponseData.ResponseError.SERVER_ERROR -> {
                            hideVacancy()
                            binding.serverErrorPlaceholderGroup.isVisible = true
                        }

                        ResponseData.ResponseError.NOT_FOUND -> {
                            hideVacancy()
                            binding.notFoundPlaceholderGroup.isVisible = true
                        }
                    }
                }

                VacancyScreenState.Loading -> {
                    hidePlaceholders()
                    startProgressBar()
                }

                is VacancyScreenState.Success -> {
                    stopProgressBar()
                    setVacancyScreen(state.vacancyDetails)
                    setListeners(state.vacancyDetails)
                }
            }
        }
    }


    private fun startProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun stopProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun setListeners(vacancyDetails: VacancyDetails) {
        binding.vacancyFavoriteIcon.setOnClickListener {
            viewModel.onLikeClicked(vacancyDetails)
        }
    }


    private fun hidePlaceholders() {
        hideVacancy()
        binding.noInternetPlaceholderGroup.isVisible = false
        binding.serverErrorPlaceholderGroup.isVisible = false
        binding.notFoundPlaceholderGroup.isVisible = false
    }

    private fun hideVacancy() {
        binding.vacancyDetailsLayout.isVisible = false
    }

    private fun setVacancyScreen(vacancyDetails: VacancyDetails) {
        binding.positionTitle.text = vacancyDetails.name
        binding.salaryTitle.text = converterSalaryToString(
            vacancyDetails.salary?.from,
            vacancyDetails.salary?.to,
            vacancyDetails.salary?.currency
        )
        binding.companyTitle.text = vacancyDetails.employer?.name
        binding.area.text = vacancyDetails.area?.name
        Glide.with(this)
            .load(vacancyDetails.employer?.logoUrls?.logo240)
            .placeholder(R.drawable.ic_placeholder_30px)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.dimen_12dp)
                )
            )
            .into(binding.itemLogo)
        binding.experience.text = vacancyDetails.experience?.name
        binding.scheduleEmployment.text = vacancyDetails.employment?.name
        binding.dutiesSubtitle.text = Html.fromHtml(vacancyDetails.description, Html.FROM_HTML_MODE_COMPACT)
        if (vacancyDetails.keySkills?.isNotEmpty() == true) {
            binding.keySkillsGroup.isVisible = true
            var keySkillsText = ""
            vacancyDetails.keySkills.forEach {
                keySkillsText += StringBuilder().append(it.name).append("\n").toString()
            }
            binding.coreSkills.text = keySkillsText
        }
        if (vacancyDetails.contacts?.name?.isNotEmpty() == true ||
            vacancyDetails.contacts?.phones?.isNotEmpty() == true
        ) {
            binding.contactsGroup.isVisible = true
            binding.contactName.text = vacancyDetails.contacts.name
            binding.contactEmail.text = vacancyDetails.contacts.email
            var telephone = ""
            var comment = ""
            vacancyDetails.contacts.phones?.forEach {
                telephone += StringBuilder().append(it.number).append("\n")
                comment += StringBuilder().append(it.comment).append("\n")
            }
            binding.contactPhone.text = telephone
            binding.contactComment.text = comment
        } else {
            binding.contactsGroup.isVisible = false
        }

        binding.vacancyDetailsLayout.isVisible = true
    }

}
