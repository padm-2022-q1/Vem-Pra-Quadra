package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListBinding
import com.reis.vinicius.vempraquadra.model.feed.FeedListItemAdapter
import com.reis.vinicius.vempraquadra.model.match.MatchListItemAdapter

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
    }
}