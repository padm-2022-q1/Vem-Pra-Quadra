package com.reis.vinicius.vempraquadra.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedBinding
import com.reis.vinicius.vempraquadra.model.adapter.FeedItemAdapter

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

        binding.recyclerFeed.adapter = FeedItemAdapter()
    }
}