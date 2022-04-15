package com.reis.vinicius.vempraquadra.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reis.vinicius.vempraquadra.databinding.FragmentChatItemBinding

class ChatItemFragment : Fragment() {
    private lateinit var binding: FragmentChatItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatItemBinding.inflate(inflater, container, false)

        return binding.root
    }
}