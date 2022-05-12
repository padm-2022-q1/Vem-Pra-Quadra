package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListBinding
import com.reis.vinicius.vempraquadra.model.feed.FeedListItemAdapter
import com.reis.vinicius.vempraquadra.model.match.Match
import com.reis.vinicius.vempraquadra.model.match.MatchListItemAdapter
import com.reis.vinicius.vempraquadra.view.home.HomeFragmentDirections
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel

class MatchListFragment : Fragment() {
    private lateinit var binding: FragmentMatchListBinding
    private val viewModel: MatchViewModel by activityViewModels()

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
        refresh()
    }

    private fun bindRefreshEvent(){ binding.matchSwipeRefreshLayout.setOnRefreshListener { refresh() } }

    private fun bindFabEvents(){
        binding.fabCreateMatch.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.openMatchAdd())
        }
    }

    private fun refresh(){
        viewModel.getAll().observe(viewLifecycleOwner){ status ->
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
                    val matches = (status.result as MainViewModel.Result.Data<List<Match>>).obj
                    val adapter = MatchListItemAdapter(matches)

                    binding.recyclerMatchList.swapAdapter(adapter, false)
                    binding.matchSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}