package com.reis.vinicius.vempraquadra.view.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reis.vinicius.vempraquadra.model.adapter.ChatListItemAdapter
import com.reis.vinicius.vempraquadra.databinding.FragmentChatListBinding

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.recyclerChat.adapter = ChatListItemAdapter()
    }
}