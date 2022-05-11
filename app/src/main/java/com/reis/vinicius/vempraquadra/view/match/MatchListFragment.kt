package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListBinding
import com.reis.vinicius.vempraquadra.model.feed.FeedListItemAdapter
import com.reis.vinicius.vempraquadra.model.match.MatchListItemAdapter
import com.reis.vinicius.vempraquadra.view.home.HomeFragmentDirections

class MatchListFragment : Fragment() {
    private lateinit var binding: FragmentMatchListBinding

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

        binding.recyclerMatchList.adapter = MatchListItemAdapter()
        bindFabEvents()
    }

    private fun bindFabEvents(){
        binding.fabCreateMatch.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.openMatchAdd())
        }
    }
}