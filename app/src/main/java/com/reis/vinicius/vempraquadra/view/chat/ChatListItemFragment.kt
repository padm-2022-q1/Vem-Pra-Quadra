package com.reis.vinicius.vempraquadra.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reis.vinicius.vempraquadra.databinding.FragmentChatListItemBinding

class ChatListItemFragment : Fragment() {
    private lateinit var binding: FragmentChatListItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListItemBinding.inflate(inflater, container, false)

        return binding.root
    }
}