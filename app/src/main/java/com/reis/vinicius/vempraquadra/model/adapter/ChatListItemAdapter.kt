package com.reis.vinicius.vempraquadra.model.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentChatListItemBinding
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.view.home.MainMenuFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class ChatListItemAdapter(
    private val context: Context,
    private val navController: NavController,
    private val chats: List<Chat>,
    private val userId: String
) : RecyclerView.Adapter<ChatListItemAdapter.Holder>() {
    inner class Holder(itemBinding: FragmentChatListItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val name = itemBinding.textChatItemContactName
            val lastMessageContent = itemBinding.textChatItemLastMessageSummary
            val lastMessageDate = itemBinding.textChatItemLastMessageDate
            val unreadIndicator = itemBinding.imageUnreadMessage

            init {
                itemBinding.root.setOnClickListener {
                    navController.navigate(MainMenuFragmentDirections.openChatDetails(
                        getId(bindingAdapterPosition))
                    )
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FragmentChatListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chat = chats[position]
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        holder.name.text = chat.match?.name ?: ""

        if (chat.lastMessage == null){
            holder.name.setTypeface(null, Typeface.NORMAL)
            holder.lastMessageContent.setTypeface(null, Typeface.ITALIC)
            holder.lastMessageContent.text = context.getString(R.string.chat_no_messages_warning)
            holder.lastMessageDate.text = ""
            holder.unreadIndicator.visibility = View.INVISIBLE
        } else {
            holder.lastMessageContent.text = chat.lastMessage.content.substring(0,
                minOf(chat.lastMessage.content.length, 40))
            holder.lastMessageDate.text = if (chat.lastMessage.sentIn != null)
                dateFormatter.format(chat.lastMessage.sentIn) else "Sending..."

            if (chat.lastMessage.readByIds.contains(userId)){
                holder.name.setTypeface(null, Typeface.NORMAL)
                holder.lastMessageContent.setTypeface(null, Typeface.NORMAL)
                holder.unreadIndicator.visibility = View.INVISIBLE
            } else {
                holder.name.setTypeface(null, Typeface.BOLD)
                holder.lastMessageContent.setTypeface(null, Typeface.BOLD)
                holder.unreadIndicator.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = chats.size

    fun getId(position: Int): String = chats[position].id
}