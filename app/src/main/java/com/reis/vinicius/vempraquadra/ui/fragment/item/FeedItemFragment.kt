package com.reis.vinicius.vempraquadra.ui.fragment.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedItemBinding

class FeedItemFragment : Fragment() {
    private lateinit var binding: FragmentFeedItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedItemBinding.inflate(inflater, container, false)

        return binding.root
    }
}