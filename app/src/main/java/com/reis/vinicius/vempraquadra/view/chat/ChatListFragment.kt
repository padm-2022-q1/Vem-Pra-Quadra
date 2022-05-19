package com.reis.vinicius.vempraquadra.view.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.databinding.FragmentChatListBinding
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.viewModel.ChatViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private val viewModel: ChatViewModel by activityViewModels()
    private val auth = Firebase.auth

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

        refresh()
        bindRefreshEvent()
    }

    private fun refresh(){
        viewModel.getAllByUser(auth.currentUser?.uid ?: "").observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading ->
                    binding.chatSwipeRefreshLayout.isRefreshing = true
                is MainViewModel.Status.Failure -> {
                    binding.chatSwipeRefreshLayout.isRefreshing = false
                    Log.e("FRAGMENT", "Failed to update courts list", status.e)
                    Snackbar.make(binding.root, "Failed to update courts list",
                        Snackbar.LENGTH_LONG).show()
                }
                is MainViewModel.Status.Success -> {
                    val chats = (status.result as MainViewModel.Result.Data<List<Chat>>).obj
                    val adapter = ChatListItemAdapter(
                        requireContext(),
                        findNavController(),
                        chats.sortedByDescending { it.lastMessage?.sentIn },
                        auth.currentUser?.uid ?: ""
                    )

                    binding.recyclerChat.swapAdapter(adapter, false)
                    binding.chatSwipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun bindRefreshEvent(){ binding.chatSwipeRefreshLayout.setOnRefreshListener { refresh() } }
}