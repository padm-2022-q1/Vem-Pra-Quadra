package com.reis.vinicius.vempraquadra.view.match

import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.BuildConfig
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchDetailsBinding
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel
import java.text.SimpleDateFormat
import java.util.*

class MatchDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailsBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private val args: MatchDetailsFragmentArgs by navArgs()
    private val auth = Firebase.auth
    private val matchWithCourtCache = MutableLiveData<MatchWithCourt>()
    private lateinit var appBar: MaterialToolbar
    private lateinit var placesClient: PlacesClient
    private var joinedMatch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = requireActivity().findViewById(R.id.app_bar_main_menu)
        appBar.menu.clear()
    }

    override fun onStart() {
        super.onStart()

        toggleLayout(false)
        readMatchData()
        bindJoinMatchEvent()
    }

    private fun bindJoinMatchEvent(){
        binding.btnMatchDetailsChangeAttendance.setOnClickListener {
            viewModel.changeAttendance(
                matchWithCourtCache.value?.match?.id ?: "",
                auth.currentUser?.uid ?: "",
                !joinedMatch
            ).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> {
                        binding.btnMatchDetailsChangeAttendance.isEnabled = false
                    }
                    is MainViewModel.Status.Failure -> {
                        binding.btnMatchDetailsChangeAttendance.isEnabled = true
                        Log.e("FRAGMENT", "Failed to join match", status.e)
                        showMessage("Failed to join match. Please, try again later.")
                    }
                    is MainViewModel.Status.Success -> {
                        joinedMatch = !joinedMatch
                        toggleJoinMatchButton(!joinedMatch)
                        binding.btnMatchDetailsChangeAttendance.isEnabled = true
                    }
                }
            }
        }
    }

    private fun readMatchData(){
        viewModel.getMatchWithCourt(args.matchId).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading -> toggleLayout(false)
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to show match details", status.e)
                    showMessage(status.e.message)
                }
                is MainViewModel.Status.Success -> {
                    val matchWithCourt = (status.result as MainViewModel.Result.Data<MatchWithCourt>).obj

                    matchWithCourtCache.value = matchWithCourt
                    joinedMatch = matchWithCourt.match.usersIds.contains(auth.currentUser?.uid ?: "")
                    fillDetails()
                }
            }
        }
    }

    private fun fillDetails(){
        matchWithCourtCache.observe(viewLifecycleOwner) { matchWithCourt ->
            val dateFormatter = SimpleDateFormat(getString(R.string.datetime_format), Locale.US)

            binding.textMatchDetailsName.text = matchWithCourt.match.name
            binding.textMatchDetailsDate.text = dateFormatter.format(matchWithCourt.match.date)
            binding.textMatchDetailsCourtName.text = matchWithCourt.court.name
            binding.textMatchDetailsAddress.text = matchWithCourt.court.address

            toggleJoinMatchButton(!matchWithCourt.match.usersIds.any { it == auth.currentUser?.uid })
            toggleLayout(true)
            fillMapFragment()
        }
    }

    private fun fillMapFragment(){
        val mapFragment = binding.fragmentContainedMatchDetailsMap.getFragment<SupportMapFragment>()

        mapFragment.getMapAsync { googleMap ->
            val geocoder = Geocoder(requireContext())
            val address = geocoder
                .getFromLocationName(matchWithCourtCache.value?.court?.address, 1).firstOrNull()

            if (address == null)
                showMessage("Court not found on map")
            else {
                val latLng = LatLng(address.latitude, address.longitude)

                googleMap.addMarker(
                    MarkerOptions().position(latLng).title(matchWithCourtCache.value?.court?.name)
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
            }
        }
    }

    private fun toggleJoinMatchButton(show: Boolean){
        if (show){
            binding.btnMatchDetailsChangeAttendance.text = getString(R.string.join_match)
            binding.btnMatchDetailsChangeAttendance.setIconResource(R.drawable.ic_check)
            binding.btnMatchDetailsChangeAttendance.iconTint = ColorStateList.valueOf(Color.WHITE)
            binding.btnMatchDetailsChangeAttendance.setTextColor(Color.WHITE)
            binding.btnMatchDetailsChangeAttendance.backgroundTintList =
                ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.purple_500, null))
        } else {
            binding.btnMatchDetailsChangeAttendance.text = getString(R.string.leave_match)
            binding.btnMatchDetailsChangeAttendance.setIconResource(R.drawable.ic_close)
            binding.btnMatchDetailsChangeAttendance.backgroundTintList = ColorStateList.valueOf(Color.RED)
            binding.btnMatchDetailsChangeAttendance.setTextColor(Color.WHITE)
            binding.btnMatchDetailsChangeAttendance.iconTint = ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun toggleLayout(show: Boolean) {
        binding.matchDetailsLayout.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.progressMatchDetails.visibility = if (!show) View.VISIBLE else View.INVISIBLE
    }

    private fun showMessage(message: String?) {
        Snackbar.make(binding.root, message ?: "Please, try again later",
            Snackbar.LENGTH_LONG).show()
    }
}