package com.reis.vinicius.vempraquadra.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedBinding
import com.reis.vinicius.vempraquadra.model.adapter.FeedListItemAdapter
import com.reis.vinicius.vempraquadra.view.home.MainMenuFragmentDirections

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindNewPostFabEvent()
        binding.recyclerFeed.adapter = FeedListItemAdapter()
    }

    private fun bindNewPostFabEvent(){
        binding.fabFeedCreatePost.setOnClickListener {
            getNavController().navigate(MainMenuFragmentDirections.openPostDialog())
        }
    }

    private fun getNavController() = requireActivity().findNavController(R.id.nav_host_home)
}