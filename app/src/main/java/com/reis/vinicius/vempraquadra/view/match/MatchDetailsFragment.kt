package com.reis.vinicius.vempraquadra.view.match

import android.content.res.ColorStateList
import android.graphics.Color
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchDetailsBinding
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel

class MatchDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailsBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private val args: MatchDetailsFragmentArgs by navArgs()
    private val auth = Firebase.auth
    private val matchCache = MutableLiveData<MatchWithCourt>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onStart() {
        super.onStart()

        toggleLayout(false)
        readMatchData()
        bindJoinMatchEvent()
    }

    private fun bindJoinMatchEvent(){
        binding.btnMatchDetailsChangeAttendance.setOnClickListener {
            viewModel.changeAttendance(
                matchCache.value?.match?.id ?: "",
                auth.currentUser?.uid ?: "",
                !((matchCache.value?.match?.usersIds?.contains(auth.currentUser?.uid ?: "")) ?: true)
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
                        toggleJoinMatchButton(false)
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
                    val match = (status.result as MainViewModel.Result.Data<MatchWithCourt>).obj

                    matchCache.value = match
                    fillDetails()
                }
            }
        }
    }

    private fun fillDetails(){
        matchCache.observe(viewLifecycleOwner) { match ->
            binding.textMatchDetailsName.text = match.match.name
            binding.textMatchDetailsAddress.text = match.court.address

            toggleJoinMatchButton(!match.match.usersIds.any { it == auth.currentUser?.uid })
            toggleLayout(true)

            // TODO("Fill map fragment")
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