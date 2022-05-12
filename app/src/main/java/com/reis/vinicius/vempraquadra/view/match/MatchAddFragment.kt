package com.reis.vinicius.vempraquadra.view.match

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchAddBinding
import com.reis.vinicius.vempraquadra.model.court.Court
import com.reis.vinicius.vempraquadra.model.court.CourtDropdownListAdapter
import com.reis.vinicius.vempraquadra.model.match.Match
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MatchAddFragment : Fragment() {
    private lateinit var binding: FragmentMatchAddBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private var selectedCourtId = 0L
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select match date")
        .setNegativeButtonText("Cancel")
        .setPositiveButtonText("Save")
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeCourtList()
        bindButtonsEvents()
        bindFormEvents()
        bindDatePickerEvents()
    }

    private fun composeCourtList(){
        viewModel.getAllCourts().observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading -> binding.inputLayoutMatchCourt.isEnabled = false
                is MainViewModel.Status.Failure -> {
                    binding.inputLayoutMatchCourt.isEnabled = true
                    Log.e("FRAGMENT", "Failed to get court list", status.e)
                    Snackbar.make(binding.root, "Failed to get court list",
                        Snackbar.LENGTH_LONG).show()
                    emptyList<Court>()
                }
                is MainViewModel.Status.Success -> {
                    val courts = (status.result as MainViewModel.Result.Data<List<Court>>).obj
                    val adapter = CourtDropdownListAdapter(requireContext(),
                        R.layout.dropdown_list_item, R.id.text_item_name, courts)

                    binding.autoCompleteMatchCourt.setAdapter(adapter)
                    binding.inputLayoutMatchCourt.isEnabled = true
                }
            }
        }
    }

    private fun bindButtonsEvents(){
        binding.btnMatchCancel.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.btnMatchSave.setOnClickListener {
            viewModel.insert(parseMatchData()).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> {
                        binding.btnMatchSave.isEnabled = false
                    }
                    is MainViewModel.Status.Failure -> {
                        binding.btnMatchSave.isEnabled = true
                        Log.e("FRAGMENT", "Failed to save match", status.e)
                        Snackbar.make(binding.root, "Failed to save match",
                            Snackbar.LENGTH_LONG).show()
                    }
                    is MainViewModel.Status.Success -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun bindDatePickerEvents(){
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val calendar = Calendar.getInstance().timeInMillis

        datePicker.addOnPositiveButtonClickListener {
            binding.textInputMatchDate.setText(formatter.format(Date(it)))
            datePicker.dismiss()
        }

        datePicker.addOnNegativeButtonClickListener { datePicker.dismiss() }
    }

    private fun parseMatchData() : Match {
        val dateParser = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        return Match(
            id = "",
            name = binding.textInputMatchName.text.toString(),
            date = dateParser.parse(binding.textInputMatchDate.text.toString()) ?: Date(),
            courtId = selectedCourtId
        )
    }

    private fun bindFormEvents(){
        lifecycle.coroutineScope.launch {
            viewModel.isSubmitEnabled.collect { value ->
                binding.btnMatchSave.isEnabled = value
            }
        }

        binding.textInputMatchDate.setOnClickListener {
            datePicker.show(parentFragmentManager, null)
        }

        binding.textInputMatchName.addTextChangedListener { text ->
            viewModel.setName(text.toString())

            binding.textLayoutMatchName.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.textInputMatchDate.addTextChangedListener { text ->
            viewModel.setDate(text.toString())

            binding.textLayoutMatchName.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.autoCompleteMatchCourt.setOnItemClickListener { _, _, position, id ->
            viewModel.setCourt(id)
            selectedCourtId = id
        }
    }
}