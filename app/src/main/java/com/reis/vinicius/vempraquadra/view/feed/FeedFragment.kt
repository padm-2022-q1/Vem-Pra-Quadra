package com.reis.vinicius.vempraquadra.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedBinding
import com.reis.vinicius.vempraquadra.model.feed.FeedListItemAdapter

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
            parentFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.nav_host_main, PostDialogFragment())
                addToBackStack(null)
            }
        }
    }
}