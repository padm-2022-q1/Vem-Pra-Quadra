package com.reis.vinicius.vempraquadra.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentChatItemBinding

class ChatItemAdapter : RecyclerView.Adapter<ChatItemAdapter.ChatItemHolder>() {
    inner class ChatItemHolder(itemBinding: FragmentChatItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val profilePicImage = itemBinding.imageChatItemProfilePic
            val name = itemBinding.textChatItemContactName
            val lastMessage = itemBinding.textChatItemLastMessageSummary
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemHolder {
        return ChatItemHolder(FragmentChatItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ChatItemHolder, position: Int) {
        return
    }

    override fun getItemCount(): Int {
        return 10
    }
}