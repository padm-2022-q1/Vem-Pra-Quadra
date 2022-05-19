package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListBinding
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.view.home.MainMenuFragmentDirections
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel
import java.util.*

class MatchListFragment : Fragment() {
    private lateinit var binding: FragmentMatchListBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private lateinit var matchesCache: List<MatchWithCourt>
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindFabEvents()
        bindRefreshEvent()
        bindChipSelectEvents()
        refresh()
    }

    private fun bindRefreshEvent(){
        binding.matchSwipeRefreshLayout.setOnRefreshListener { refresh() }
    }

    private fun bindFabEvents(){
        binding.fabCreateMatch.setOnClickListener {
            findNavController().navigate(MainMenuFragmentDirections.openMatchAdd())
        }
    }

    private fun bindChipSelectEvents(){
        binding.chipsMatchList.setOnCheckedStateChangeListener { _, _ ->
            swapMatchesAdapter()
        }
    }

    private fun refresh(){
        viewModel.getAllMatchWithCourts().observe(viewLifecycleOwner){ status ->
            when (status) {
                is MainViewModel.Status.Loading ->
                    binding.matchSwipeRefreshLayout.isRefreshing = true
                is MainViewModel.Status.Failure -> {
                    binding.matchSwipeRefreshLayout.isRefreshing = false
                    Log.e("FRAGMENT", "Failed to fetch matches", status.e)
                    Snackbar.make(binding.root, "Failed to update match list",
                        Snackbar.LENGTH_LONG).show()
                }
                is MainViewModel.Status.Success -> {
                    matchesCache = (status.result as MainViewModel.Result.Data<List<MatchWithCourt>>)
                        .obj.sortedBy { it.match.date }

                    swapMatchesAdapter()
                    binding.matchSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun swapMatchesAdapter() {
        val filteredMatches = when (binding.chipsMatchList.checkedChipId) {
            R.id.chip_filter_all -> matchesCache.filter { it.match.date >= Date() }
            R.id.chip_filter_past -> matchesCache.filter { it.match.date < Date() }
            R.id.chip_filter_joined -> matchesCache.filter {
                it.match.usersIds.contains(auth.currentUser?.uid ?: "")
            }
            R.id.chip_filter_not_joined -> matchesCache.filter {
                !it.match.usersIds.contains(auth.currentUser?.uid ?: "")
            }
            else -> matchesCache
        }

        val adapter = MatchListItemAdapter(requireContext(), findNavController(),
            filteredMatches.sortedBy { it.match.date })

        binding.recyclerMatchList.swapAdapter(adapter, false)
    }
}