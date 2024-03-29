package com.reis.vinicius.vempraquadra.view.court

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentCourtListBinding
import com.reis.vinicius.vempraquadra.model.entity.Court
import com.reis.vinicius.vempraquadra.view.home.MainMenuFragmentDirections
import com.reis.vinicius.vempraquadra.viewModel.CourtViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel

class CourtListFragment : Fragment() {
    private lateinit var binding: FragmentCourtListBinding
    private val viewModel: CourtViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourtListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindRefreshEvent()
        bindFabEvents()
        refresh()
    }

    private fun bindFabEvents(){
        binding.fabMatchesCreate.setOnClickListener {
            getNavController().navigate(MainMenuFragmentDirections.openCourtAdd())
        }
    }

    private fun bindRefreshEvent(){ binding.courtSwipeRefreshLayout.setOnRefreshListener { refresh() } }

    private fun refresh(){
        viewModel.getAll().observe(viewLifecycleOwner){ status ->
            when (status) {
                is MainViewModel.Status.Loading ->
                    binding.courtSwipeRefreshLayout.isRefreshing = true
                is MainViewModel.Status.Failure -> {
                    binding.courtSwipeRefreshLayout.isRefreshing = false
                    Log.e("FRAGMENT", "Failed to update courts list", status.e)
                    Snackbar.make(binding.root, "Failed to update courts list",
                        Snackbar.LENGTH_LONG).show()
                }
                is MainViewModel.Status.Success -> {
                    val courts = (status.result as MainViewModel.Result.Data<List<Court>>).obj
                    val adapter = CourtListItemAdapter(this, courts)

                    binding.recyclerCourts.swapAdapter(adapter, false)
                    binding.courtSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun getNavController() =
        requireActivity().findNavController(R.id.nav_host_home)
}