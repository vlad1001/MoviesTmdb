package com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.BottomSheetFilterBinding
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterKey.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class FilterBottomSheet(
    private val initialFilterParams: FilterParams,
    var listener: (response: FilterParams) -> Unit
) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetFilterBinding
    private val viewModel: FilterBottomSheetViewModel by viewModels()
    private val DATE_PICKER_TAG = "DATE_PICKER_TAG"

//    var listener: (response: FilterParams) -> Unit = {}

    private val filterParams: FilterParams
        get() = viewModel.filerParams

    constructor() : this(FilterParams(), {})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!initialFilterParams.isEmpty()) {
            viewModel.filerParams = initialFilterParams
        }

        binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilterBinding()

        Handler(Looper.getMainLooper()).postDelayed({
            setLastSelectedValues()
        }, 1)
    }

    private fun setLastSelectedValues() {
        binding.userScoreRangeSlider.setValues(
            filterParams.user_min_score.toFloat(),
            filterParams.user_max_score.toFloat()
        )
        binding.runtimeSlider.setValues(
            filterParams.duration_min.toFloat(),
            filterParams.duration_max.toFloat()
        )
        binding.userVotesRangeSlider.value = filterParams.user_min_votes.toFloat()

        binding.datePickerFrom.text =
            filterParams.dateFrom.ifEmpty {
                "From Date"
            }

        binding.datePickerTo.text = filterParams.dateTo.ifEmpty {
            "To Date"
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.language_list_item,
            viewModel.getLanguageList(requireContext())
        )

        (binding.languageMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.languageAutoComplete.setText(viewModel.getLanguageList(requireContext())[2], false)

        binding.bottomSheetChipGroup.forEach { chip ->
            if (filterParams.genres.contains(chip.id)) {
                (chip as Chip).isChecked = true
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        return dialog
    }

    private fun getLatestValues() {
//        val ids = currentlySelectedChips.joinToString(separator = ",")
//        selectedFilter["vote_count.gte"] = "${binding.userVotesRangeSlider.value}"
//        selectedFilter["vote_average.gte"] = "${binding.userScoreRangeSlider.values[0]}"
//        selectedFilter["vote_average.lte"] = "${binding.userScoreRangeSlider.values[1]}"
//        selectedFilter["with_runtime.gte"] = "${binding.runtimeSlider.values[0]}"
//        selectedFilter["with_runtime.lte"] = "${binding.runtimeSlider.values[1]}"
//        val language = binding.languageMenu.editText?.text.toString()
//        if (language.isNotEmpty()) {
//            selectedFilter["language"] = language
//        }
    }

    private fun addChips(chipGroup: ChipGroup, chips: List<Genre>) {
        chips.forEach { genre ->
            val chip = ChipBinding.inflate(LayoutInflater.from(chipGroup.context)).root
            chip.id = genre.id
            chip.text = genre.name
            chip.setOnCheckedChangeListener { compountBtn, ischecked ->
                updateChipStatus(compountBtn, ischecked)
            }
            chipGroup.addView(chip)
        }
    }

    private fun updateChipStatus(compountBtn: CompoundButton, isChecked: Boolean) {
        if (!isChecked && filterParams.genres.contains(compountBtn.id)) {
            viewModel.removeFilter(CHOSEN_GENRES, compountBtn.id)
        } else {
            viewModel.addFilter(CHOSEN_GENRES, compountBtn.id)
        }
    }

    private fun initFilterBinding() {
        binding.closeBottomSheet.setOnClickListener {
            dismiss()
        }

        (binding.languageMenu.editText as? AutoCompleteTextView)?.doOnTextChanged { text, start, before, count ->
            viewModel.addFilter(LANGUAGE, text.toString())
        }

        binding.userScoreRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.addFilter(USER_MIN_SCORE, "${slider.values[0]}")
            viewModel.addFilter(USER_MAX_SCORE, "${slider.values[1]}")
        }

        binding.userVotesRangeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.addFilter(USER_VOTES, "$value")
        }

        binding.runtimeSlider.addOnChangeListener { slider, value, fromUser ->
            viewModel.addFilter(USER_MIN_RUNTIME, "${slider.values[0]}")
            viewModel.addFilter(USER_MAX_RUNTIME, "${slider.values[1]}")
        }

        launchAndRepeatWithViewLifecycle {
            binding.bottomSheetChipGroup.removeAllViews()
            viewModel.geners.collect { genres ->
                addChips(binding.bottomSheetChipGroup, genres)
            }
        }

        binding.datePickerFrom.setOnClickListener {
            //val calendarFrom = getDate(true)
            //datePicker(calendarFrom, binding.datePickerFrom, true)
            val date =
                if (filterParams.dateFrom.isEmpty()) {
                    Calendar.getInstance().timeInMillis
                } else {
                    filterParams.dateFrom.toLong()
                }

            showDatePicker(
                date,
                binding.datePickerFrom,
                true,
                timeCallBack
            )
        }

        binding.datePickerTo.setOnClickListener {
//            val calendarTo = getDate(false)
//            datePicker(calendarTo, binding.datePickerTo, false)

            val date =
                if (filterParams.dateTo.isEmpty()) {
                    Calendar.getInstance().timeInMillis
                } else {
                    filterParams.dateTo.toLong()
                }

            showDatePicker(
                date,
                binding.datePickerTo,
                false,
                timeCallBack
            )
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.language_list_item,
            viewModel.getLanguageList(requireContext())
        )
        (binding.languageMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.languageAutoComplete.setText(viewModel.getLanguageList(requireContext())[0], false)

        binding.filterBtn.setOnClickListener {
            listener(viewModel.filerParams)
            dismiss()
        }
    }

    private val timeCallBack: (
        mills: Long,
        formatted: String,
        from: Boolean
    ) -> Unit = { milis, formatted, from ->
        if (from) {
            viewModel.addFilter(FROM_DATE, formatted.replace("-",""))
        } else {
            viewModel.addFilter(TO_DATE, formatted.replace("-",""))
        }
    }

    private fun showDatePicker(
        initialValue: Long,
        presenterTextView: TextView,
        from: Boolean,
        onDateSelected: (mills: Long, formatted: String, from: Boolean) -> Unit
    ) {

        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val calender = Calendar.getInstance()
        calender.timeInMillis =
            initialValue.takeIf { it > 0 } ?: MaterialDatePicker.todayInUtcMilliseconds()
//        presenterTextView.text = formatter.format(calender.time)

        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(calender.timeInMillis)
            .build().let { datePicker ->

                datePicker.addOnPositiveButtonClickListener {
                    calender.timeInMillis = it
                    presenterTextView.text = formatter.format(calender.time)
                    onDateSelected(it, formatter.format(calender.time), from)
                    datePicker.dismiss()
                }

                datePicker.show(parentFragmentManager, DATE_PICKER_TAG)
            }
    }

//    val date = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(requireContext())
//        .format(LocalDate.of(year, month,day))

    private fun datePicker(date: LocalDate, textView: TextView, from: Boolean) {
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, month, day ->
            val chosenDate: LocalDate = LocalDate.of(year, month, day)
            when (from) {
                true -> {
//                    selectedFilter["release_date.gte"] = "$chosenDate"
                    viewModel.addFilter(FROM_DATE, "$chosenDate")

                }
                false -> {
//                    selectedFilter["release_date.lte"] = "$chosenDate"
                    viewModel.addFilter(TO_DATE, "$chosenDate")

                }
            }
            textView.text =
                "${chosenDate.year} ${chosenDate.month.value.plus(1)} ${chosenDate.dayOfMonth}"
        }, date.year, date.month.value, date.year)

        //datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()

    }

    private fun getDate(from: Boolean): LocalDate {
        val strDate: String? = null

        if (from && filterParams.dateFrom.isNotEmpty()) {
            viewModel.addFilter(FROM_DATE, filterParams.dateFrom)
        } else if (!from && filterParams.dateTo.isNotEmpty()) {
            viewModel.addFilter(TO_DATE, filterParams.dateTo)
        }

        return if (strDate != null) {
            LocalDate.parse(strDate)
        } else {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            LocalDate.of(year, month, day)
        }
    }
}
