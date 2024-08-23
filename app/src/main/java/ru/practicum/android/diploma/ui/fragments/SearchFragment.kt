package ru.practicum.android.diploma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.state.SearchScreenState
import ru.practicum.android.diploma.util.FILTER_REQUEST_KEY
import ru.practicum.android.diploma.util.ResponseData
import ru.practicum.android.diploma.util.adapter.VacancyAdapter

class SearchFragment : Fragment() {
    companion object {
        const val VACANCY_KEY = "VACANCY_KEY"
    }

    private val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<SearchViewModel>()

    private val adapter = VacancyAdapter({ setOnItemClicked(it) })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.vacanciesLiveData.observe(viewLifecycleOwner) { vacancies ->
            adapter.setVacancies(vacancies)
            binding.textUnderSearch.text = getCorrectAmountText(vacancies.size)
            binding.textUnderSearch.isVisible = vacancies.isNotEmpty()
            binding.searchDefaultPlaceholder.isGone = vacancies.isNotEmpty()
        }

        binding.searchRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecycleView.adapter = adapter

        binding.filterIc.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        binding.searchQuery.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.searchIconLoupe.isVisible = false
                binding.clearCrossIc.isVisible = true
                binding.searchDefaultPlaceholder.isVisible = false
                viewModel.searchDebounce(binding.searchQuery.text.toString())
            } else {
                viewModel.setRequest("")
                binding.searchIconLoupe.isVisible = true
                binding.clearCrossIc.isVisible = false
            }

        }

        binding.clearCrossIc.setOnClickListener {
            binding.searchQuery.text.clear()
        }

        binding.searchRecycleView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.searchRecycleView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }

        })

        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchScreenState.Default -> {
                    setFilterIcon()
                }

                is SearchScreenState.Error -> {
                    clearList()
                    removePlaceholders()
                    when (state.error) {
                        ResponseData.ResponseError.NO_INTERNET -> {
                            showNoInternetState()
                        }

                        ResponseData.ResponseError.CLIENT_ERROR,
                        ResponseData.ResponseError.SERVER_ERROR -> {
                            showServerError()
                        }

                        ResponseData.ResponseError.NOT_FOUND -> {
                            showNothingFoundState()
                        }
                    }
                }

                is SearchScreenState.LoadNextPage -> {
                    binding.progressBarNextPage.isVisible = false
                }

                SearchScreenState.Loading -> {
                    clearList()
                    removePlaceholders()
                    startProgressBar()
                }

                is SearchScreenState.NothingFound -> {
                    clearList()
                    showNothingFoundState()
                }

                is SearchScreenState.Success -> {
                    stopProgressBar()
                    removePlaceholders()
                }

                SearchScreenState.LoadingNextPage -> {
                    binding.progressBarNextPage.isVisible = true
                }

                is SearchScreenState.ErrorNextPage -> {
                    binding.progressBarNextPage.isVisible = false
                    when (state.error) {
                        ResponseData.ResponseError.NO_INTERNET -> {
                            showMessage(getString(R.string.next_page_internet_error))
                        }

                        ResponseData.ResponseError.CLIENT_ERROR,
                        ResponseData.ResponseError.SERVER_ERROR,
                        ResponseData.ResponseError.NOT_FOUND -> {
                            showMessage(getString(R.string.next_page_server_error))
                        }
                    }
                }
            }
        }

        binding.searchQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val viewEdit = activity?.currentFocus
                if (viewEdit != null) {
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            false
        }
        viewModel.getOptions()

        initResultListeners()
    }

    private fun initResultListeners() {
        setFragmentResultListener(FILTER_REQUEST_KEY) { _, _ ->
            if (viewModel.getMainRequest().isNotEmpty()) {
                viewModel.setDefaultCurrentPage()
                viewModel.search(true)
            }
        }
    }

    private fun clearList() {
        adapter.setVacancies(emptyList())
    }

    private fun removePlaceholders() {
        binding.noVacancyToShow.isVisible = false
        binding.noVacancyToShowText.isVisible = false
        binding.noConnectionText.isVisible = false
        binding.noConnectionPlaceholder.isVisible = false
        binding.serverError.isVisible = false
        binding.serverErrorText.isVisible = false
    }

    private fun setFilterIcon() {
        if (viewModel.isFilter()) {
            binding.filterIc.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_filter_on_24px
                )
            )
        } else {
            binding.filterIc.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_filter_off_12px
                )
            )
        }
    }

    private fun startProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun stopProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun showNoInternetState() {
        stopProgressBar()
        binding.noConnectionPlaceholder.isVisible = true
        binding.noConnectionText.isVisible = true
    }

    private fun showNothingFoundState() {
        stopProgressBar()
        binding.noVacancyToShow.isVisible = true
        binding.noVacancyToShowText.isVisible = true
        binding.textUnderSearch.setText(R.string.there_no_such_vacancies)
        binding.textUnderSearch.isVisible = true
    }

    private fun showServerError() {
        stopProgressBar()
        binding.serverError.isVisible = true
        binding.serverErrorText.isVisible = true
    }

    private fun setOnItemClicked(vacancy: Vacancy) {
        if (viewModel.vacancyIsClickable.value == false) return
        viewModel.onVacancyClicked()
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            bundleOf(VACANCY_KEY to vacancy.id)
        )
    }

    private fun getCorrectAmountText(quantity: Int): String {
        return requireContext().resources.getQuantityString(
            R.plurals.vacancy_quantity,
            quantity,
            quantity
        )
    }

    private fun showMessage(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setTextColor(requireContext().getColor(R.color.white))
            .setBackgroundTint(requireContext().getColor(R.color.red))
            .setTextMaxLines(2)
        val view = snackbar.view
        val textView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }
}
