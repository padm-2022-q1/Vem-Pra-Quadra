package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListBinding

class MatchListItemFragment : Fragment() {
    private lateinit var binding: FragmentMatchListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchListBinding.inflate(inflater, container, false)

        return binding.root
    }
}