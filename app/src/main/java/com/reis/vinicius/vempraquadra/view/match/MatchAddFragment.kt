package com.reis.vinicius.vempraquadra.view.match

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleExpandableListAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchAddBinding
import com.reis.vinicius.vempraquadra.model.entity.Court
import com.reis.vinicius.vempraquadra.model.adapter.CourtDropdownListAdapter
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.hypot

class MatchAddFragment : Fragment() {
    private lateinit var binding: FragmentMatchAddBinding
    private val auth = Firebase.auth
    private val viewModel: MatchViewModel by activityViewModels()
    private var selectedCourtId = 0L
    private val selectedDate = Date()
    private lateinit var appBar: MaterialToolbar
    private val datePicker = buildDatePicker()
    private val timePicker =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

    override fun onStart() {
        super.onStart()

        setHasOptionsMenu(true)
    }

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

        appBar = requireActivity().findViewById(R.id.app_bar_main_menu)
        appBar.inflateMenu(R.menu.menu_match_add)

        composeCourtList()
        bindAppBarItemEvent()
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

    private fun bindAppBarItemEvent(){
        appBar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.menu_item_match_save ->
                    viewModel.insert(parseMatchData()).observe(viewLifecycleOwner) { status ->
                        when (status) {
                            is MainViewModel.Status.Loading -> { item.isEnabled = false }
                            is MainViewModel.Status.Failure -> {
                                item.isEnabled = true
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

            true
        }
    }

    private fun bindDatePickerEvents(){
        val formatter = SimpleDateFormat(getString(R.string.datetime_format), Locale.US)
        val calendar = Calendar.getInstance()

        datePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            datePicker.dismiss()
            timePicker.show(parentFragmentManager, null)
        }

        datePicker.addOnNegativeButtonClickListener { datePicker.dismiss() }

        timePicker.addOnPositiveButtonClickListener {
            calendar[Calendar.HOUR] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute
            binding.textInputMatchDate.setText(formatter.format(calendar.time))
            timePicker.dismiss()
        }

        timePicker.addOnNegativeButtonClickListener { timePicker.dismiss() }
    }

    private fun parseMatchData() : Match {
        val dateParser = SimpleDateFormat(getString(R.string.datetime_format), Locale.US)

        return Match(
            id = "",
            name = binding.textInputMatchName.text.toString(),
            date = dateParser.parse(binding.textInputMatchDate.text.toString()) ?: Date(),
            courtId = selectedCourtId,
            usersIds = arrayListOf( auth.currentUser?.uid ?: "" )
        )
    }

    private fun bindFormEvents(){
        lifecycle.coroutineScope.launch {
            viewModel.isSubmitEnabled.collect { value ->
                appBar.menu.findItem(R.id.menu_item_match_save).isEnabled = value
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

            val parser = SimpleDateFormat(getString(R.string.datetime_format), Locale.US)
            val selectedDate = parser.parse(text.toString())

            if (selectedDate != null) {
                binding.textLayoutMatchDate.error =
                    if (text.isNullOrEmpty())
                        getString(R.string.warning_required_field)
                    else if (selectedDate < Date())
                        "Match time can not be in the past"
                    else null
            }
        }

        binding.autoCompleteMatchCourt.setOnItemClickListener { _, _, position, id ->
            viewModel.setCourt(id)
            selectedCourtId = id

            val court = binding.autoCompleteMatchCourt.adapter.getItem(position)

            fillMapFragment(court as Court)
        }
    }

    private fun fillMapFragment(court: Court){
        val mapFragment = binding.matchAddMapFragmentContainer.getFragment<SupportMapFragment>()

        mapFragment.getMapAsync { googleMap ->
            val geocoder = Geocoder(requireContext())
            val address = geocoder
                .getFromLocationName(court.address, 1).firstOrNull()

            if (address == null)
                showMessage("Court not found on map")
            else {
                val latLng = LatLng(address.latitude, address.longitude)

                googleMap.addMarker(MarkerOptions().position(latLng).title(court.name))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
            }
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select match date")
            .setNegativeButtonText("Cancel")
            .setPositiveButtonText("Save")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private fun showMessage(message: String?) {
        Snackbar.make(binding.root, message ?: "Please, try again later",
            Snackbar.LENGTH_LONG).show()
    }
}