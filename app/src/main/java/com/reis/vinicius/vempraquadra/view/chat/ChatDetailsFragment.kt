package com.reis.vinicius.vempraquadra.view.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentChatDetailsBinding
import com.reis.vinicius.vempraquadra.model.dto.ChatWithMessages
import com.reis.vinicius.vempraquadra.model.dto.MessageWithUserData
import com.reis.vinicius.vempraquadra.model.entity.Message
import com.reis.vinicius.vempraquadra.viewModel.ChatViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import java.util.*

class ChatDetailsFragment : Fragment() {
    private lateinit var binding: FragmentChatDetailsBinding
    private val viewModel: ChatViewModel by activityViewModels()
    private val args: ChatDetailsFragmentArgs by navArgs()
    private val auth = Firebase.auth
    private val chatCache = MutableLiveData<ChatWithMessages>()
    private lateinit var appBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

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

        appBar.inflateMenu(R.menu.menu_chat_details)
    }

    override fun onStart() {
        super.onStart()

        loadMessages()
        bindSendButtonEvent()
        bindFormEvents()
    }

    private fun bindFormEvents(){
        binding.textInputChatMessageContent.addTextChangedListener { text ->
            binding.btnChatSendMessage.isEnabled = !text.isNullOrEmpty()
        }

        appBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item_chat_update ->
                    loadMessages()
            }

            true
        }
    }

    private fun bindSendButtonEvent(){
        binding.btnChatSendMessage.setOnClickListener {
            viewModel.saveMessage(Message(
                "",
                binding.textInputChatMessageContent.text.toString(),
                Date(),
                auth.currentUser?.uid ?: "",
                chatCache.value?.chat?.id ?: "",
                arrayListOf( auth.currentUser?.uid ?: "" )
            )).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> toggleMessages(false)
                    is MainViewModel.Status.Failure -> {
                        toggleMessages(true)
                        Log.e("FRAGMENT", "Failed to save message", status.e)
                        showMessage(status.e.message)
                    }
                    is MainViewModel.Status.Success -> {
                        val message = (status.result as MainViewModel.Result.Data<MessageWithUserData>).obj

                        (binding.recyclerViewChatMessages.adapter as MessageListItemAdapter).addMessage(message)
                        binding.textInputChatMessageContent.text = null
                        toggleMessages(true)
                    }
                }
            }
        }
    }

    private fun loadMessages(){
        viewModel.getWithMessagesById(args.chatId, auth.currentUser?.uid ?: "")
            .observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading -> toggleMessages(false)
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to get messages", status.e)
                    showMessage(status.e.message)
                    toggleMessages(true)
                }
                is MainViewModel.Status.Success -> {
                    val chat = (status.result as MainViewModel.Result.Data<ChatWithMessages>).obj

                    chatCache.value = chat
                    fillDetails()
                    toggleMessages(true)
                }
            }
        }
    }

    private fun fillDetails(){
        chatCache.observe(viewLifecycleOwner) { chat ->
            val adapter = MessageListItemAdapter(
                chatCache.value?.messages?.sortedByDescending { it.message.sentIn }?.toMutableList()
                    ?: emptyList<MessageWithUserData>().toMutableList(),
                auth.currentUser?.uid ?: ""
            )

            binding.recyclerViewChatMessages.swapAdapter(adapter, false)
            appBar.title = chat.chat.match?.name ?: "Chat"
        }
    }

    private fun toggleMessages(show: Boolean){
        binding.recyclerViewChatMessages.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.progressChatDetails.visibility = if (!show) View.VISIBLE else View.INVISIBLE
    }

    private fun showMessage(message: String?) {
        Snackbar.make(binding.root, message ?: "Please, try again later",
            Snackbar.LENGTH_LONG).show()
    }
}