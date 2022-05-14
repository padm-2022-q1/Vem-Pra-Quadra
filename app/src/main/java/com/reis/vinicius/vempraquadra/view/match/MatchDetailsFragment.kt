package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchDetailsBinding
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.model.entity.Match
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
        binding.btnMatchDetailsJoin.setOnClickListener {
            viewModel.joinMatch(matchCache.value?.match?.id ?: "",
                auth.currentUser?.uid ?: "").observe(viewLifecycleOwner) { status ->
                    when (status) {
                        is MainViewModel.Status.Loading -> {
                            binding.btnMatchDetailsJoin.isEnabled = false
                        }
                        is MainViewModel.Status.Failure -> {
                            binding.btnMatchDetailsJoin.isEnabled = true
                            Log.e("FRAGMENT", "Failed to join match", status.e)
                            showSnackbar("Failed to join match. Please, try again later.")
                        }
                        is MainViewModel.Status.Success -> {
                            toggleJoinMatchButton(false)
                            binding.btnMatchDetailsJoin.isEnabled = true
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
                    showSnackbar(status.e.message)
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
        }
    }

    private fun toggleJoinMatchButton(show: Boolean){
        binding.btnMatchDetailsJoin.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.btnMatchDetailsLeave.visibility = if (!show) View.VISIBLE else View.INVISIBLE
    }

    private fun toggleLayout(show: Boolean) {
        binding.matchDetailsLayout.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.progressMatchDetails.visibility = if (!show) View.VISIBLE else View.INVISIBLE
    }

    private fun showSnackbar(message: String?) {
        Snackbar.make(binding.root, message ?: "Please, try again later",
            Snackbar.LENGTH_LONG).show()
    }
}