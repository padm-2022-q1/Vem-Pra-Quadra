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
import com.reis.vinicius.vempraquadra.databinding.FragmentChatMessageSentBinding
import com.reis.vinicius.vempraquadra.model.entity.Chat
import java.text.SimpleDateFormat
import java.util.*

class MessageListItemAdapter(
    private val context: Context,
    private val navController: NavController,
    private val chats: List<Chat>,
    private val userId: String
) : RecyclerView.Adapter<MessageListItemAdapter.Holder>() {
    inner class MessageSentHolder(itemBinding: FragmentChatMessageSentBinding):
        // TODO("Create view holders")

        RecyclerView.ViewHolder(itemBinding.root){
            val name = itemBinding.textChatItemContactName
            val lastMessageContent = itemBinding.textChatItemLastMessageSummary
            val lastMessageDate = itemBinding.textChatItemLastMessageDate
            val unreadIndicator = itemBinding.imageUnreadMessage

            init {
                itemBinding.root.setOnClickListener {
                    // TODO("Navigate to chat details")
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

        if (chat.lastMessage != null){
            holder.name.setTypeface(null, Typeface.BOLD)
            holder.lastMessageContent.setTypeface(null, Typeface.BOLD)
            holder.lastMessageDate.text = dateFormatter.format(chat.lastMessage.sentIn)
            holder.lastMessageContent.text = chat.lastMessage.content.substring(40)
            holder.lastMessageDate.text = dateFormatter.format(chat.lastMessage.sentIn)
            holder.unreadIndicator.visibility =
                if (chat.lastMessage.readByIds.contains(userId)) View.VISIBLE else View.INVISIBLE
        } else {
            holder.name.setTypeface(null, Typeface.NORMAL)
            holder.lastMessageContent.setTypeface(null, Typeface.ITALIC)
            holder.lastMessageContent.text = context.getString(R.string.chat_no_messages_warning)
            holder.lastMessageDate.text = ""
            holder.unreadIndicator.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = chats.size

    fun getId(position: Int): String = chats[position].id
}