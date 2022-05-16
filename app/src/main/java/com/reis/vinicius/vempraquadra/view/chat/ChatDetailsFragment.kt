package com.reis.vinicius.vempraquadra.view.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentChatDetailsBinding
import com.reis.vinicius.vempraquadra.databinding.FragmentChatListBinding
import com.reis.vinicius.vempraquadra.model.dto.ChatWithMessages
import com.reis.vinicius.vempraquadra.viewModel.ChatViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel

class ChatDetailsFragment : Fragment() {
    private lateinit var binding: FragmentChatDetailsBinding
    private val viewModel: ChatViewModel by activityViewModels()
    private val args: ChatDetailsFragmentArgs by navArgs()
    private val chatCache = MutableLiveData<ChatWithMessages>()
    private lateinit var appBar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appBar = requireActivity().findViewById(R.id.app_bar_main_menu)
    }

    override fun onStart() {
        super.onStart()

        loadMessages()
    }

    private fun loadMessages(){
        viewModel.getWithMessagesById(args.chatId).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading -> toggleMessages(false)
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to get messages", status.e)
                    showMessage(status.e.message)
                }
                is MainViewModel.Status.Success -> {
                    val chat = (status.result as MainViewModel.Result.Data<ChatWithMessages>).obj

                    chatCache.value = chat
                    fillDetails()
                }
            }
        }
    }

    private fun fillDetails(){
        chatCache.observe(viewLifecycleOwner) { chat ->
            appBar.title = chat.chat.match?.name ?: "Chat"
            fillMessageList()
        }
    }

    private

    private fun toggleMessages(show: Boolean){
        binding.recyclerViewChatMessages.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.progressChatDetails.visibility = if (!show) View.VISIBLE else View.INVISIBLE
    }

    private fun showMessage(message: String?) {
        Snackbar.make(binding.root, message ?: "Please, try again later",
            Snackbar.LENGTH_LONG).show()
    }
}