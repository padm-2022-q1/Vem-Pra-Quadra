package com.reis.vinicius.vempraquadra.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentChatListItemBinding

class ChatListItemAdapter : RecyclerView.Adapter<ChatListItemAdapter.Holder>() {
    inner class Holder(itemBinding: FragmentChatListItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val name = itemBinding.textChatItemContactName
            val lastMessage = itemBinding.textChatItemLastMessageSummary
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FragmentChatListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return
    }

    override fun getItemCount(): Int {
        return 10
    }
}